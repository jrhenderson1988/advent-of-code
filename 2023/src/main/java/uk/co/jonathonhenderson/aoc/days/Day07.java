package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1);

    private final int rating;

    Outcome(int rating) {
      this.rating = rating;
    }

    public int getRating() {
      return rating;
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
  }

  private record Hand(List<Card> cards, long bid) implements Comparable<Hand> {

    public static Hand parse(String line) {
      var parts = line.trim().split("\\s+");
      var cards = parts[0].chars().mapToObj(ch -> new Card((char) ch)).toList();
      var bid = Long.parseLong(parts[1]);
      return new Hand(cards, bid);
    }

    @Override
    public String toString() {
      return cards.stream()
          .map(c -> c.card())
          .map(c -> String.valueOf(c))
          .collect(Collectors.joining());
    }

    public Outcome getOutcome() {
      var m = new HashMap<Character, Integer>();
      for (var card : this.cards) {
        m.compute(card.card(), (k, v) -> v == null ? 1 : v + 1);
      }

      var values = m.values().stream().sorted().toList();
      if (values.equals(List.of(5))) {
        return Outcome.FIVE_OF_A_KIND;
      } else if (values.equals(List.of(1, 4))) {
        return Outcome.FOUR_OF_A_KIND;
      } else if (values.equals(List.of(2, 3))) {
        return Outcome.FULL_HOUSE;
      } else if (values.equals(List.of(1, 1, 3))) {
        return Outcome.THREE_OF_A_KIND;
      } else if (values.equals(List.of(1, 2, 2))) {
        return Outcome.TWO_PAIR;
      } else if (values.equals(List.of(1, 1, 1, 2))) {
        return Outcome.ONE_PAIR;
      } else {
        return Outcome.HIGH_CARD;
      }
    }

    @Override
    public int compareTo(Hand o) {
      var thisRating = this.getOutcome().getRating();
      var otherRating = o.getOutcome().getRating();
      if (thisRating < otherRating) {
        return -1;
      } else if (thisRating > otherRating) {
        return 1;
      }

      for (var i = 0; i < this.cards().size(); i++) {
        var thisValue = this.cards().get(i).value();
        var otherValue = o.cards().get(i).value();
        if (thisValue < otherValue) {
          return -1;
        } else if (thisValue > otherValue) {
          return 1;
        }
      }

      return 0;
    }
  }

  private record Game(List<Hand> hands) {
    public static Game parse(String input) {
      return new Game(input.trim().lines().map(Hand::parse).toList());
    }

    public long getTotalWinnings() {
      var ranked = hands.stream().sorted().toList();
      return LongStream.range(0, ranked.size())
          .mapToObj(i -> (i + 1) * ranked.get((int) i).bid())
          .reduce(Long::sum)
          .orElseThrow();
    }

    public long getTotalWinningsWithJokers() {
      var ranked = hands.stream().sorted().toList();
      return LongStream.range(0, ranked.size())
          .mapToObj(i -> (i + 1) * ranked.get((int) i).bid())
          .reduce(Long::sum)
          .orElseThrow();
    }
  }
}
