package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day17Test {
  private static final String INPUT =
      """
      2413432311323
      3215453535623
      3255245654254
      3446585845452
      4546657867536
      1438598798454
      4457876987766
      3637877979653
      4654967986887
      4564679986453
      1224686865563
      2546548887735
      4322674655533
      """;

  private static final String INPUT2 =
      """
      111111111111
      999999999991
      999999999991
      999999999991
      999999999991
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("102"), new Day17(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("94"), new Day17(INPUT).part2());
    assertEquals(Optional.of("71"), new Day17(INPUT2).part2());
  }
}
