package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day16Test {
  private static final String INPUT =
      """
      .|...\\....
      |.-.\\.....
      .....|-...
      ........|.
      ..........
      .........\\
      ..../.\\\\..
      .-.-/..|..
      .|....-|.\\
      ..//.|....
      """;

  @Test
  void part1() {
    assertEquals(Optional.of("46"), new Day16(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.empty(), new Day16(INPUT).part2());
  }
}
