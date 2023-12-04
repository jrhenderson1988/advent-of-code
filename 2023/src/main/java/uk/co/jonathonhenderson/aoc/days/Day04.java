package uk.co.jonathonhenderson.aoc.days;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04 extends Day {

  private final List<Card> cards;

  public Day04(String input) {
    this.cards = Card.parseCards(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(cards.stream().map(Card::getValue).reduce(Integer::sum).orElseThrow());
  }

  @Override
  public Optional<String> part2() {
    return answer(play().stream().map(Card::copies).reduce(Integer::sum).orElseThrow());
  }

  private List<Card> play() {
    var cards = this.cards.stream().collect(Collectors.toMap(Card::id, c -> c));
    for (var id : this.cards.stream().map(Card::id).toList()) {
      var card = cards.get(id);
      var totalWinningNumbers = card.totalWinningNumbers();
      var copies = card.copies();
      for (var i = 1; i <= totalWinningNumbers; i++) {
        var copiedId = id + i;
        var copiedCard = cards.get(copiedId);
        var newCard =
            new Card(
                copiedId,
                copiedCard.winningNumbers(),
                copiedCard.drawnNumbers(),
                copiedCard.copies() + copies);
        cards.put(copiedId, newCard);
      }
    }

    return cards.values().stream().toList();
  }

  private record Card(
      int id, List<Integer> winningNumbers, List<Integer> drawnNumbers, int copies) {
    public static List<Card> parseCards(String input) {
      return input.trim().lines().map(Card::parseCard).toList();
    }

    private static Card parseCard(String line) {
      var parts = line.trim().split(":");
      var id = Integer.parseInt(parts[0].split("\\s+")[1].trim());
      var numbers =
          Stream.of(parts[1].split("\\s*\\|\\s*"))
              .map(String::trim)
              .map(
                  chunk ->
                      Stream.of(chunk.split("\\s+"))
                          .map(String::trim)
                          .map(Integer::parseInt)
                          .toList())
              .toList();
      if (numbers.size() != 2) {
        throw new IllegalArgumentException("Could not parse");
      }
      return new Card(id, numbers.get(0), numbers.get(1), 1);
    }

    public int totalWinningNumbers() {
      return drawnNumbers.stream().filter(winningNumbers::contains).toList().size();
    }

    public int getValue() {
      return (int) Math.pow(2, totalWinningNumbers() - 1);
    }
  }
}
