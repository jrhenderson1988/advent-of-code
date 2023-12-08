package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
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
    return answer(instructions.countStepsToEndAsGhost());
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
        current = takeStep(current, i);
        i++;
      }
      return i;
    }

    public long countStepsToEndAsGhost() {
      var positions = this.nodes.keySet().stream().filter(k -> k.endsWith("A")).toList();

      var totals = new ArrayList<Long>();
      for (var position : positions) {
        var i = 0;
        var current = position;
        while (!current.endsWith("Z")) {
          current = takeStep(current, i);
          i++;
        }
        totals.add((long)i);
      }

      return totals.stream().reduce(this::lcm).orElseThrow();
    }

    private String takeStep(String current, int step) {
      var node = this.nodes.get(current);
      var direction = directions.get(step % directions.size());
      return switch (direction) {
        case LEFT -> node.left();
        case RIGHT -> node.right();
      };
    }

    private long lcm(long number1, long number2) {
      if (number1 == 0 || number2 == 0) {
        return 0;
      }
      var absNumber1 = Math.abs(number1);
      var absNumber2 = Math.abs(number2);
      var absHigherNumber = Math.max(absNumber1, absNumber2);
      var absLowerNumber = Math.min(absNumber1, absNumber2);
      var lcm = absHigherNumber;
      while (lcm % absLowerNumber != 0) {
        lcm += absHigherNumber;
      }
      return lcm;
    }
  }
}
