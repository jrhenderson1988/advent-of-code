package uk.co.jonathonhenderson.aoc.days;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class Day12Test {
  private static final String INPUT =
      """
      ???.### 1,1,3
      .??..??...?##. 1,1,3
      ?#?#?#?#?#?#?#? 1,3,1,6
      ????.#...#... 4,1,1
      ????.######..#####. 1,6,5
      ?###???????? 3,2,1
      """;

  private static final String INPUT2 = "????.#...#... 4,1,1";

  @Test
  void part1() {
    assertEquals(Optional.of("21"), new Day12(INPUT).part1());
  }

  @Test
  void part2() {
    assertEquals(Optional.empty(), new Day12(INPUT).part2());
  }
}
