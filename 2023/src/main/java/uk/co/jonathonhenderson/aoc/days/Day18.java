package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day18 extends Day {

  private final DigPlan digPlan;

  public Day18(String input) {
    this.digPlan = DigPlan.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(digPlan.calculateCapacityOfLagoon());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private record Color(int red, int green, int blue) {
    public static Color parse(String input) {
      var numbers = input.trim().replace("(", "").replace(")", "").replace("#", "").trim();
      if (numbers.length() != 6) {
        throw new IllegalArgumentException();
      }
      var red = Integer.parseInt(numbers.substring(0, 2), 16);
      var green = Integer.parseInt(numbers.substring(2, 4), 16);
      var blue = Integer.parseInt(numbers.substring(4, 6), 16);
      return new Color(red, green, blue);
    }
  }

  private record Instruction(Direction direction, int amount, Color color) {
    public static Instruction parse(String line) {
      var parts = line.trim().split(" ");
      var direction = parseDirection(parts[0]);
      var amount = Integer.parseInt(parts[1].trim());
      var color = Color.parse(parts[2]);
      return new Instruction(direction, amount, color);
    }

    private static Direction parseDirection(String value) {
      return switch (value.trim()) {
        case "U" -> Direction.NORTH;
        case "R" -> Direction.EAST;
        case "D" -> Direction.SOUTH;
        case "L" -> Direction.WEST;
        default -> throw new IllegalArgumentException("Invalid direction");
      };
    }
  }

  private record DigPlan(List<Instruction> instructions) {
    public static DigPlan parse(String input) {
      return new DigPlan(input.trim().lines().map(Instruction::parse).toList());
    }

    public int calculateCapacityOfLagoon() {
      var edges = calculateEdges();

      var min = getMin(edges);
      var newMin = Point.of(min.x() - 1, min.y() - 1);
      var max = getMax(edges);
      var newMax = Point.of(max.x() + 1, max.y() + 1);
      var outer = fillOutside(edges, newMin, newMax);

      var total = 0;
      for (var y = min.y(); y <= max.y(); y++) {
        for (var x = min.x(); x <= max.x(); x++) {
          if (!outer.contains(Point.of(x, y))) {
            total += 1;
          }
        }
      }

      return total;
    }

    private Set<Point> fillOutside(Set<Point> edges, Point min, Point max) {
      var cells = new HashSet<Point>();
      var queue = new ArrayDeque<>(List.of(min));
      while (!queue.isEmpty()) {
        var curr = queue.poll();
        if (curr.x() < min.x() || curr.x() > max.x() || curr.y() < min.y() || curr.y() > max.y()) {
          continue;
        } else if (cells.contains(curr)) {
          continue;
        } else if (edges.contains(curr)) {
          continue;
        }

        cells.add(curr);
        for (var dir : Direction.values()) {
          var next = curr.translate(dir.delta());
          queue.add(next);
        }
      }

      return cells;
    }

    private Set<Point> calculateEdges() {
      var curr = Point.of(0, 0);

      var allEdges = new HashSet<>(Set.of(curr));

      for (var instruction : instructions) {
        var direction = instruction.direction();
        var delta = direction.delta();
        for (var i = 0; i < instruction.amount(); i++) {
          curr = curr.translate(delta);
          allEdges.add(curr);
        }
      }

      return allEdges;
    }

    private Point getMin(Set<Point> edges) {
      return edges.stream()
          .reduce((a, b) -> Point.of(Math.min(a.x(), b.x()), Math.min(a.y(), b.y())))
          .orElseThrow();
    }

    private Point getMax(Set<Point> edges) {
      return edges.stream()
          .reduce((a, b) -> Point.of(Math.max(a.x(), b.x()), Math.max(a.y(), b.y())))
          .orElseThrow();
    }

  }
}
