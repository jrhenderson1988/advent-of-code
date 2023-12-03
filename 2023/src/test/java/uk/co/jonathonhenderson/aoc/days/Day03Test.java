package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day03Test {
  private final String INPUT =
      """
          467..114..
          ...*......
          ..35..633.
          ......#...
          617*......
          .....+.58.
          ..592.....
          ......755.
          ...$.*....
          .664.598..""";

  @Test
  void part1() {
    assertEquals(Optional.of("4361"), new Day03(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("467835"), new Day03(INPUT).part2());
  }
}
