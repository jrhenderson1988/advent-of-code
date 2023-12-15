package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class Day15Test {
  private static final String INPUT = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

  @Test
  void part1() {
    assertEquals(Optional.of("1320"), new Day15(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.of("145"), new Day15(INPUT).part2());
  }
}
