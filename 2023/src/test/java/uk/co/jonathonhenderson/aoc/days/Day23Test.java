package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day23Test {
  private static final String INPUT =
      """
      #.#####################
      #.......#########...###
      #######.#########.#.###
      ###.....#.>.>.###.#.###
      ###v#####.#v#.###.#.###
      ###.>...#.#.#.....#...#
      ###v###.#.#.#########.#
      ###...#.#.#.......#...#
      #####.#.#.#######.#.###
      #.....#.#.#.......#...#
      #.#####.#.#.#########v#
      #.#...#...#...###...>.#
      #.#.#v#######v###.###v#
      #...#.>.#...>.>.#.###.#
      #####v#.#.###v#.#.###.#
      #.....#...#...#.#.#...#
      #.#########.###.#.#.###
      #...###...#...#...#.###
      ###.###.#.###v#####v###
      #...#...#.#.>.>.#.>.###
      #.###.###.#.###.#.#v###
      #.....###...###...#...#
      #####################.#
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("94"), new Day23(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("154"), new Day23(INPUT).part2());
  }
}
