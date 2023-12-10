package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class Day10Test {
  private static final String INPUT =
      """
      .....
      .S-7.
      .|.|.
      .L-J.
      .....
      """;

  private static final String INPUT2 =
      """
      ..F7.
      .FJ|.
      SJ.L7
      |F--J
      LJ...
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("4"), new Day10(INPUT).part1());
     assertEquals(Optional.of("8"), new Day10(INPUT2).part1());
  }

  @Test
  void part2() {}
}
