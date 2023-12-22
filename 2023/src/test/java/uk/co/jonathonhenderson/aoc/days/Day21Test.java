package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day21Test {
  private static final String INPUT =
      """
      ...........
      .....###.#.
      .###.##..#.
      ..#.#...#..
      ....#.#....
      .##..S####.
      .##..#...#.
      .......##..
      .##.#.####.
      .##..##.##.
      ...........
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("16"), new Day21(INPUT).part1(6));
  }

  @Test
  void part2() {
    assertEquals(Optional.of("50"), new Day21(INPUT).part2(10));
    assertEquals(Optional.of("1594"), new Day21(INPUT).part2(50));
    assertEquals(Optional.of("6536"), new Day21(INPUT).part2(100));
    assertEquals(Optional.of("167004"), new Day21(INPUT).part2(500));
    assertEquals(Optional.of("668697"), new Day21(INPUT).part2(1000));
    assertEquals(Optional.of("16733044"), new Day21(INPUT).part2(5000));
  }
}
