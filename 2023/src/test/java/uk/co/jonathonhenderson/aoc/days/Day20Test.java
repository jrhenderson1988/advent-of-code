package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day20Test {
  private static final String INPUT =
      """
      broadcaster -> a, b, c
      %a -> b
      %b -> c
      %c -> inv
      &inv -> a
      """;

  private static final String INPUT2 =
      """
      broadcaster -> a
      %a -> inv, con
      &inv -> b
      %b -> con
      &con -> output
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("32000000"), new Day20(INPUT).part1());
    assertEquals(Optional.of("11687500"), new Day20(INPUT2).part1());
  }

  @Test
  void part2() {
    // Nothing to test
    assertEquals(Optional.empty(), new Day20(INPUT).part2());
  }
}
