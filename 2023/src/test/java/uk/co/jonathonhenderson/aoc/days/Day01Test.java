package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day01Test {
  private static final String INPUT =
      """
      1abc2
      pqr3stu8vwx
      a1b2c3d4e5f
      treb7uchet""";

  private static final String INPUT2 =
      """
      two1nine
      eightwothree
      abcone2threexyz
      xtwone3four
      4nineeightseven2
      zoneight234
      7pqrstsixteen""";

  private final Day01 day01 = new Day01();

  @Test
  void part1() {
    assertEquals(Optional.of("142"), day01.part1(INPUT));
  }

  @Test
  void part2() {
    assertEquals(Optional.of("281"), day01.part2(INPUT2));
  }
}
