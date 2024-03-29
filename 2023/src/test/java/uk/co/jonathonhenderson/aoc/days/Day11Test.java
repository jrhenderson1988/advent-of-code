package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class Day11Test {
  private static final String INPUT =
      """
      ...#......
      .......#..
      #.........
      ..........
      ......#...
      .#........
      .........#
      ..........
      .......#..
      #...#.....
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("374"), new Day11(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("1030"), new Day11(INPUT).part2(10));
    assertEquals(Optional.of("8410"), new Day11(INPUT).part2(100));
  }
}
