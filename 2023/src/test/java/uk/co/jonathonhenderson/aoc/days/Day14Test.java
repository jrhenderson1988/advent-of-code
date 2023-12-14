package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day14Test {
  private static final String INPUT =
      """
      O....#....
      O.OO#....#
      .....##...
      OO.#O....O
      .O.....O#.
      O.#..O.#.#
      ..O..#O..O
      .......O..
      #....###..
      #OO..#....
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("136"), new Day14(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.empty(), new Day14(INPUT).part2());
  }
}
