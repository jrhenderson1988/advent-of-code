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
    // Nothing to test
    assertEquals(Optional.empty(), new Day21(INPUT).part2());
  }
}
