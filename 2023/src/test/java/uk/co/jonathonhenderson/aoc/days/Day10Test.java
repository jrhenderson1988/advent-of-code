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

  private static final String INPUT3 =
      """
      ...........
      .S-------7.
      .|F-----7|.
      .||.....||.
      .||.....||.
      .|L-7.F-J|.
      .|..|.|..|.
      .L--J.L--J.
      ...........
      """;

  //      """
  //      .............
  //      .S-------7...
  //      .|F-----7|...
  //      .||.....||...
  //      .||.....||...
  //      .|L-7.F-JL-7.
  //      .|..|.|..F-J.
  //      .L--J.L--J...
  //      .............

  private static final String INPUT4 =
      """
      .F----7F7F7F7F-7....
      .|F--7||||||||FJ....
      .||.FJ||||||||L7....
      FJL7L7LJLJ||LJ.L-7..
      L--J.L7...LJS7F-7L7.
      ....F-J..F7FJ|L7L7L7
      ....L7.F7||L7|.L7L7|
      .....|FJLJ|FJ|F7|.LJ
      ....FJL-7.||.||||...
      ....L---J.LJ.LJLJ...
      """;

  private static final String INPUT5 =
      """
      FF7FSF7F7F7F7F7F---7
      L|LJ||||||||||||F--J
      FL-7LJLJ||||||LJL-77
      F--JF--7||LJLJ7F7FJ-
      L---JF-JLJ.||-FJLJJ7
      |F|F-JF---7F7-L7L|7|
      |FFJF7L7F-JF7|JL---7
      7-L-JL7||F7|L7F-7F7|
      L.L7LFJ|||||FJL7||LJ
      L7JLJL-JLJLJL--JLJ.L
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("4"), new Day10(INPUT).part1());
    assertEquals(Optional.of("8"), new Day10(INPUT2).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("4"), new Day10(INPUT3).part2());
    assertEquals(Optional.of("8"), new Day10(INPUT4).part2());
    assertEquals(Optional.of("10"), new Day10(INPUT5).part2());
  }
}
