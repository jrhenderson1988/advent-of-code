package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day09Test {
  private final String INPUT =
      """
      0 3 6 9 12 15
      1 3 6 10 15 21
      10 13 16 21 30 45
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("114"), new Day09(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of(""), new Day09(INPUT).part2());
  }
}
