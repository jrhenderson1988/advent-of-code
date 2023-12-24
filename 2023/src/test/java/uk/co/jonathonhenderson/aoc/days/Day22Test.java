package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day22Test {
  private static final String INPUT =
      """
      1,0,1~1,2,1
      0,0,2~2,0,2
      0,2,3~2,2,3
      0,0,4~0,2,4
      2,0,5~2,2,5
      0,1,6~2,1,6
      1,1,8~1,1,9
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("5"), new Day22(INPUT).part1());
  }

  @Test
  void part2() {}
}
