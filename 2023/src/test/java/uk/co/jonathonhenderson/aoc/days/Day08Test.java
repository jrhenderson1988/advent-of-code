package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day08Test {
  private final String INPUT =
      """
      RL

      AAA = (BBB, CCC)
      BBB = (DDD, EEE)
      CCC = (ZZZ, GGG)
      DDD = (DDD, DDD)
      EEE = (EEE, EEE)
      GGG = (GGG, GGG)
      ZZZ = (ZZZ, ZZZ)
      """;

  private final String INPUT2 =
      """
      LLR

      AAA = (BBB, BBB)
      BBB = (AAA, ZZZ)
      ZZZ = (ZZZ, ZZZ)
      """;

  private final String INPUT3 =
      """
      LR

      11A = (11B, XXX)
      11B = (XXX, 11Z)
      11Z = (11B, XXX)
      22A = (22B, XXX)
      22B = (22C, 22C)
      22C = (22Z, 22Z)
      22Z = (22B, 22B)
      XXX = (XXX, XXX)
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("2"), new Day08(INPUT).part1());
    assertEquals(Optional.of("6"), new Day08(INPUT2).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("6"), new Day08(INPUT3).part2());
  }
}
