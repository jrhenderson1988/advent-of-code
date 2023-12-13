package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day13Test {
  private static final String INPUT =
      """
      #.##..##.
      ..#.##.#.
      ##......#
      ##......#
      ..#.##.#.
      ..##..##.
      #.#.##.#.

      #...##..#
      #....#..#
      ..##..###
      #####.##.
      #####.##.
      ..##..###
      #....#..#
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("405"), new Day13(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.empty(), new Day13(INPUT).part2());
  }
}
