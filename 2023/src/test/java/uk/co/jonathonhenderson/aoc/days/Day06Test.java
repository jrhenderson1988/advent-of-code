package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day06Test {
  private final String INPUT = "Time:      7  15   30\n" + "Distance:  9  40  200";

  @Test
  void part1() {
    assertEquals(Optional.of("288"), new Day06(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("71503"), new Day06(INPUT).part2());
  }
}
