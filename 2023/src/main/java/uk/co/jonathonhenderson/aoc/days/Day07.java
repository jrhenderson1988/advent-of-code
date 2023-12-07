package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day07 extends Day {

  private final Game game;

  public Day07(String input) {
    this.game = Game.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(game.getTotalWinnings());
  }

  @Override
  public Optional<String> part2() {
    return answer(game.getTotalWinningsWithJokers());
  }

  private enum Outcome {
    FIVE_OF_A_KIND(7, List.of(5)),
    FOUR_OF_A_KIND(6, List.of(1, 4)),
    FULL_HOUSE(5, List.of(2, 3)),
    THREE_OF_A_KIND(4, List.of(1, 1, 3)),
    TWO_PAIR(3, List.of(1, 2, 2)),
    ONE_PAIR(2, List.of(1, 1, 1, 2)),
    HIGH_CARD(1, List.of(1, 1, 1, 1, 1));

    private final int rating;
    private final List<Integer> pattern;

    Outcome(int rating, List<Integer> pattern) {
      this.rating = rating;
      this.pattern = pattern;
    }

    public static Outcome fromPattern(List<Integer> pattern) {
      return Stream.of(Outcome.values())
          .filter(outcome -> outcome.matches(pattern))
          .findFirst()
          .orElseThrow();
    }

    public int getRating() {
      return rating;
    }

    public boolean matches(List<Integer> pattern) {
      return this.pattern.equals(pattern);
    }
  }

  private interface Hand extends Comparable<Hand> {
    Outcome getOutcome();

    List<Card> cards();

    long valueOfCard(Card c);

    long bid();

    default int compareTo(Hand o) {
      var thisRating = this.getOutcome().getRating();
      var otherRating = o.getOutcome().getRating();
      if (thisRating < otherRating) {
        return -1;
      } else if (thisRating > otherRating) {
        return 1;
      }

      for (var i = 0; i < this.cards().size(); i++) {
        var thisValue = this.valueOfCard(this.cards().get(i));
        var otherValue = this.valueOfCard(o.cards().get(i));
        if (thisValue < otherValue) {
          return -1;
        } else if (thisValue > otherValue) {
          return 1;
        }
      }

      return 0;
    }
  }

  private record Card(char card) {
    public long value() {
      return switch (card) {
        case 'A' -> 14;
        case 'K' -> 13;
        case 'Q' -> 12;
        case 'J' -> 11;
        case 'T' -> 10;
        case '9' -> 9;
        case '8' -> 8;
        case '7' -> 7;
        case '6' -> 6;
        case '5' -> 5;
        case '4' -> 4;
        case '3' -> 3;
        case '2' -> 2;
        default -> throw new IllegalArgumentException("Unrecognised card");
      };
    }

    public long valueWithJoker() {
      return card == 'J' ? 1 : value();
    }
  }

  private record JokerHand(NormalHand hand) implements Hand {

    private List<Integer> getPatternWithoutJokers() {
      var m = new HashMap<Character, Integer>();
      for (var card : cards()) {
        if (card.card() != 'J') {
          m.compute(card.card(), (k, v) -> v == null ? 1 : v + 1);
        }
      }
      return m.values().stream().sorted().toList();
    }

    @Override
    public Outcome getOutcome() {
      var totalJokers = cards().stream().filter(c -> c.card() == 'J').count();
      if (totalJokers == 0) {
        return hand.getOutcome();
      } else if (totalJokers == 5) {
        // all cards are jokers and can be five of a kind
        return Outcome.FIVE_OF_A_KIND;
      } else if (totalJokers == 4) {
        // all jokers can mirror the only non-joker card
        return Outcome.FIVE_OF_A_KIND;
      }

      if (totalJokers == 3) {
        // there are two other cards with possible formations:
        // [1, 1] -> joker matches one of the two cards: four of a kind
        // [2] -> joker matches all the cards: five of a kind
        var valuesWithoutJokers = getPatternWithoutJokers();
        if (valuesWithoutJokers.equals(List.of(2))) {
          return Outcome.FIVE_OF_A_KIND;
        } else {
          return Outcome.FOUR_OF_A_KIND;
        }
      } else if (totalJokers == 2) {
        // there are three other cards, with possible formations:
        // [1, 1, 1] -> both jokers match one of the cards -> three of a kind
        // [1, 2] -> both jokers match the most common card -> four of a kind
        // [3] -> both jokers match the card -> five of a kind
        var valuesWithoutJokers = getPatternWithoutJokers();
        if (valuesWithoutJokers.equals(List.of(1, 1, 1))) {
          return Outcome.THREE_OF_A_KIND;
        } else if (valuesWithoutJokers.equals(List.of(1, 2))) {
          return Outcome.FOUR_OF_A_KIND;
        } else {
          return Outcome.FIVE_OF_A_KIND;
        }
      } else {
        // there is 1 joker and therefore 4 other cards:
        // [1, 1, 1, 1] -> joker matches one of the cards -> one pair
        // [1, 1, 2] -> joker matches most common card -> three of a kind
        // [2, 2] -> joker matches one of the two cards -> full house
        // [1, 3] -> joker matches most common card -> four of a kind
        // [4] -> joker matches only card -> five of a kind
        var valuesWithoutJokers = getPatternWithoutJokers();
        if (valuesWithoutJokers.equals(List.of(1, 1, 1, 1))) {
          return Outcome.ONE_PAIR;
        } else if (valuesWithoutJokers.equals(List.of(1, 1, 2))) {
          return Outcome.THREE_OF_A_KIND;
        } else if (valuesWithoutJokers.equals(List.of(2, 2))) {
          return Outcome.FULL_HOUSE;
        } else if (valuesWithoutJokers.equals(List.of(1, 3))) {
          return Outcome.FOUR_OF_A_KIND;
        } else {
          return Outcome.FIVE_OF_A_KIND;
        }
      }
    }

    @Override
    public List<Card> cards() {
      return hand.cards();
    }

    @Override
    public long valueOfCard(Card c) {
      return c.valueWithJoker();
    }

    @Override
    public long bid() {
      return hand.bid();
    }
  }

  private record NormalHand(List<Card> cards, long bid) implements Hand {

    public static NormalHand parse(String line) {
      var parts = line.trim().split("\\s+");
      var cards = parts[0].chars().mapToObj(ch -> new Card((char) ch)).toList();
      var bid = Long.parseLong(parts[1]);
      return new NormalHand(cards, bid);
    }

    @Override
    public String toString() {
      return cards.stream().map(Card::card).map(String::valueOf).collect(Collectors.joining());
    }

    @Override
    public Outcome getOutcome() {
      var m = new HashMap<Character, Integer>();
      for (var card : this.cards) {
        m.compute(card.card(), (k, v) -> v == null ? 1 : v + 1);
      }

      return Outcome.fromPattern(m.values().stream().sorted().toList());
    }

    @Override
    public long valueOfCard(Card c) {
      return c.value();
    }
  }

  private record Game(List<NormalHand> hands) {
    public static Game parse(String input) {
      return new Game(input.trim().lines().map(NormalHand::parse).toList());
    }

    public long getTotalWinnings() {
      var ranked = hands.stream().sorted().toList();
      return LongStream.range(0, ranked.size())
          .mapToObj(i -> (i + 1) * ranked.get((int) i).bid())
          .reduce(Long::sum)
          .orElseThrow();
    }

    public long getTotalWinningsWithJokers() {
      var ranked = hands.stream().map(JokerHand::new).sorted().toList();
      return LongStream.range(0, ranked.size())
          .mapToObj(i -> (i + 1) * ranked.get((int) i).bid())
          .reduce(Long::sum)
          .orElseThrow();
    }
  }
}
