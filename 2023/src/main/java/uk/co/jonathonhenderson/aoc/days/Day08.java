package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Lines;
import uk.co.jonathonhenderson.aoc.common.Pair;
import uk.co.jonathonhenderson.aoc.common.Strings;

public class Day08 extends Day {
  private final Instructions instructions;

  public Day08(String input) {
    this.instructions = Instructions.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(instructions.countStepsToEnd());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private enum Direction {
    LEFT,
    RIGHT;

    public static Direction of(char ch) {
      return switch (ch) {
        case 'L' -> LEFT;
        case 'R' -> RIGHT;
        default -> throw new IllegalArgumentException("Invalid direction");
      };
    }
  }

  private record Instructions(List<Direction> directions, Map<String, Pair<String, String>> nodes) {
    public static Instructions parse(String input) {
      var chunks = Lines.splitByEmptyLines(input).toList();
      if (chunks.size() != 2) {
        throw new IllegalArgumentException("Invalid input");
      }

      var directions =
          chunks.get(0).trim().chars().mapToObj(ch -> Direction.of((char) ch)).toList();

      var nodes = new HashMap<String, Pair<String, String>>();
      for (var line : chunks.get(1).trim().lines().map(String::trim).toList()) {
        var lineParts = line.split("=");
        var key = lineParts[0].trim();
        var options =
            Stream.of(Strings.strip(lineParts[1].trim(), '(', ')').split(","))
                .map(String::trim)
                .toList();
        if (options.size() != 2) {
          throw new IllegalArgumentException("Invalid input");
        }
        nodes.put(key, Pair.of(options.get(0), options.get(1)));
      }

      return new Instructions(directions, nodes);
    }

    public int countStepsToEnd() {
      var current = "AAA";
      var i = 0;
      while (!current.equals("ZZZ")) {
        var options = this.nodes.get(current);
        var direction = this.directions.get(i % directions.size());

        current =
            switch (direction) {
              case LEFT -> options.left();
              case RIGHT -> options.right();
            };
        i++;
      }
      return i;
    }
  }
}
