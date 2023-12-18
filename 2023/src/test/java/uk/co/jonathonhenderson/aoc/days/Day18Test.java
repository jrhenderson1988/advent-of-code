package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day18Test {
  private static final String INPUT =
      """
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("62"), new Day18(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("952408144115"), new Day18(INPUT).part2());
    // we're 2 below the result right now
  }
}
