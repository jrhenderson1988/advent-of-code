package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day24Test {
  private static final String INPUT =
      """
      19, 13, 30 @ -2,  1, -2
      18, 19, 22 @ -1, -1, -2
      20, 25, 34 @ -2, -2, -4
      12, 31, 28 @ -1, -2, -1
      20, 19, 15 @  1, -5, -3
      """;

  @Test
  void part1() {
    assertEquals(
        Optional.of("2"), new Day24(INPUT).part1(BigDecimal.valueOf(7), BigDecimal.valueOf(27)));
  }

  @Test
  void part2() {
    assertEquals(Optional.empty(), new Day24(INPUT).part2());
  }
}
