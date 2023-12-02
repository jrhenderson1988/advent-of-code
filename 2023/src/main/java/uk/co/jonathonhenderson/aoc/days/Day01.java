package uk.co.jonathonhenderson.aoc.days;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day01 extends Day {

  private final String input;

  public Day01(String input) {
    this.input = input;
  }

  private static final Map<String, Integer> DIGITS =
      ofEntries(
          entry("0", 0),
          entry("1", 1),
          entry("2", 2),
          entry("3", 3),
          entry("4", 4),
          entry("5", 5),
          entry("6", 6),
          entry("7", 7),
          entry("8", 8),
          entry("9", 9),
          entry("zero", 0),
          entry("one", 1),
          entry("two", 2),
          entry("three", 3),
          entry("four", 4),
          entry("five", 5),
          entry("six", 6),
          entry("seven", 7),
          entry("eight", 8),
          entry("nine", 9));

  @Override
  public Optional<String> part1() {
    return Optional.of(String.valueOf(sumDigitsInLines(input, this::findNumberWithDigits)));
  }

  @Override
  public Optional<String> part2() {
    return Optional.of(String.valueOf(sumDigitsInLines(input, this::findNumberWithDigitsAndWords)));
  }

  private int sumDigitsInLines(String lines, Function<String, Integer> findNumberFn) {
    return lines.trim().lines().map(findNumberFn).reduce(0, Integer::sum);
  }

  private int findNumberWithDigitsAndWords(String line) {
    var first =
        IntStream.range(0, line.length())
            .mapToObj(i -> getDigitAt(line, i))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow();
    var last =
        IntStream.range(0, line.length())
            .mapToObj(i -> getDigitAt(line, line.length() - 1 - i))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow();
    return first * 10 + last;
  }

  private Integer getDigitAt(String line, int pos) {
    return DIGITS.entrySet().stream()
        .filter(e -> e.getKey().length() + pos <= line.length())
        .filter(e -> line.substring(pos, pos + e.getKey().length()).equalsIgnoreCase(e.getKey()))
        .map(Entry::getValue)
        .findFirst()
        .orElse(null);
  }

  private int findNumberWithDigits(String line) {
    var first =
        line.chars()
            .filter(Character::isDigit)
            .mapToObj(Character::getNumericValue)
            .findFirst()
            .orElseThrow();
    var last =
        IntStream.range(0, line.length())
            .mapToObj(i -> line.charAt(line.length() - 1 - i))
            .filter(Character::isDigit)
            .map(Character::getNumericValue)
            .findFirst()
            .orElseThrow();

    return (first * 10) + last;
  }
}
