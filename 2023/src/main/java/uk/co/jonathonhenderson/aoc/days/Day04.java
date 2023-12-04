package uk.co.jonathonhenderson.aoc.days;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04 extends Day {

  private final Game game;

  public Day04(String input) {
    this.game = Game.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(game.totalValueOfCards());
  }

  @Override
  public Optional<String> part2() {
    return answer(game.totalCardsAfterPlaying());
  }

  private record Game(List<Card> cards) {
    public static Game parse(String input) {
      return new Game(input.trim().lines().map(Card::parse).toList());
    }

    public int totalValueOfCards() {
      return cards.stream().map(Card::getValue).reduce(Integer::sum).orElseThrow();
    }

    public int totalCardsAfterPlaying() {
      return play().stream().map(Card::copies).reduce(Integer::sum).orElseThrow();
    }

    private List<Card> play() {
      var cards = this.cards.stream().collect(Collectors.toMap(Card::id, c -> c));
      for (var id : cards.keySet()) {
        var currentCard = cards.get(id);
        var currentTotalWinningNumbers = currentCard.totalWinningNumbers();
        var currentCopies = currentCard.copies();
        for (var i = 1; i <= currentTotalWinningNumbers; i++) {
          var copiedId = id + i;
          var copiedCard = cards.get(copiedId);
          var newCard =
              new Card(
                  copiedId,
                  copiedCard.winningNumbers(),
                  copiedCard.drawnNumbers(),
                  copiedCard.copies() + currentCopies);
          cards.put(copiedId, newCard);
        }
      }

      return cards.values().stream().toList();
    }
  }

  private record Card(
      int id, List<Integer> winningNumbers, List<Integer> drawnNumbers, int copies) {

    private static Card parse(String line) {
      var parts = line.trim().split(":");
      var id = Integer.parseInt(parts[0].split("\\s+")[1].trim());
      var numbers =
          Stream.of(parts[1].split("\\s*\\|\\s*"))
              .map(String::trim)
              .map(Card::parseNumbers)
              .toList();
      if (numbers.size() != 2) {
        throw new IllegalArgumentException("Could not parse");
      }
      return new Card(id, numbers.get(0), numbers.get(1), 1);
    }

    private static List<Integer> parseNumbers(String chunk) {
      return Stream.of(chunk.split("\\s+")).map(String::trim).map(Integer::parseInt).toList();
    }

    public int totalWinningNumbers() {
      return drawnNumbers.stream().filter(winningNumbers::contains).toList().size();
    }

    public int getValue() {
      return (int) Math.pow(2, totalWinningNumbers() - 1);
    }
  }
}
