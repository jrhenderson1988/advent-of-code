package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day07Test {
  private final String INPUT =
      """
          32T3K 765
          T55J5 684
          KK677 28
          KTJJT 220
          QQQJA 483""";

  @Test
  void part1() {
    assertEquals(Optional.of("6440"), new Day07(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("5905"), new Day07(INPUT).part2());
  }
}
