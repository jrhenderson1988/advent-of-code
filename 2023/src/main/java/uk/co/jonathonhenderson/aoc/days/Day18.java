package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    return answer(digPlan.calculateCapacityOfLagoonWithCorrectedPlan());
  }

  private record Color(String hex) {
    public static Color parse(String input) {
      var numbers = input.trim().replace("(", "").replace(")", "").replace("#", "").trim();
      if (numbers.length() != 6) {
        throw new IllegalArgumentException();
      }
      return new Color(numbers);
    }

    public int amount() {
      return Integer.parseInt(hex.substring(0, 5), 16);
    }

    public Direction direction() {
      return switch (hex.substring(5, 6)) {
        case "0" -> Direction.EAST;
        case "1" -> Direction.SOUTH;
        case "2" -> Direction.WEST;
        case "3" -> Direction.NORTH;
        default -> throw new IllegalArgumentException("Invalid direction");
      };
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

    public Instruction correct() {
      return new Instruction(color.direction(), color.amount(), color);
    }
  }

  private record DigPlan(List<Instruction> instructions) {
    public static DigPlan parse(String input) {
      return new DigPlan(input.trim().lines().map(Instruction::parse).toList());
    }

    public long calculateCapacityOfLagoon() {
      var vertices = findVertices();
      var area = calculateArea(vertices);
      var perimeter = perimeterSize();
      var interior = area - perimeter / 2 + 1;
      return interior + perimeter;
    }

    private long perimeterSize() {
      return instructions.stream().map(Instruction::amount).reduce(Integer::sum).orElseThrow();
    }

    public long calculateCapacityOfLagoonWithCorrectedPlan() {
      var digPlan = new DigPlan(this.instructions.stream().map(Instruction::correct).toList());
      return digPlan.calculateCapacityOfLagoon();
    }

    private long calculateArea(List<Point> vertices) {
      var area = 0L;
      for (var i = 0; i < vertices.size() - 1; i++) {
        var curr = vertices.get(i);
        var next = vertices.get(i + 1);
        area += curr.x() * next.y() - next.x() * curr.y();
      }
      return Math.abs(area) / 2;
    }

    private List<Point> findVertices() {
      var curr = Point.of(0, 0);

      var vertices = new ArrayList<>(List.of(curr));

      for (var instruction : instructions) {
        var direction = instruction.direction();
        var delta = direction.delta();
        var actualDelta =
            Point.of(delta.x() * instruction.amount(), delta.y() * instruction.amount());
        curr = curr.translate(actualDelta);
        if (!curr.equals(Point.of(0, 0))) {

          vertices.add(curr);
        }
      }

      return vertices;
    }
  }
}
