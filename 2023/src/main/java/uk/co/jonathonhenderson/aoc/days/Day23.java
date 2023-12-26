package uk.co.jonathonhenderson.aoc.days;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day23 extends Day {
  private final Grid grid;

  public Day23(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(grid.lengthOfLongestHike());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private enum Cell {
    OPEN,
    WALL,
    SLOPE_LEFT,
    SLOPE_RIGHT,
    SLOPE_DOWN,
    SLOPE_UP;

    public static Cell of(char ch) {
      return switch (ch) {
        case '.' -> OPEN;
        case '#' -> WALL;
        case '>' -> SLOPE_RIGHT;
        case '<' -> SLOPE_LEFT;
        case 'v' -> SLOPE_DOWN;
        case '^' -> SLOPE_UP;
        default -> throw new IllegalArgumentException("Unrecognised cell");
      };
    }
  }

  private record Grid(List<List<Cell>> cells) {
    public static Grid parse(String input) {
      return new Grid(
          input.trim().lines().toList().stream()
              .map(line -> line.trim().chars().mapToObj(ch -> Cell.of((char) ch)).toList())
              .toList());
    }

    public long lengthOfLongestHike() {
      var start = findStart();
      var end = findEnd();

      return dfs(start, end);
    }

    private List<Point> neighboursOf(Point point) {
      return switch (cellAt(point)) {
        case OPEN -> Arrays.stream(Direction.values())
            .map(d -> d.delta().translate(point))
            .filter(p -> !cellAt(p).equals(Cell.WALL))
            .toList();
        case WALL -> throw new IllegalStateException("Should not be reachable");
        case SLOPE_LEFT -> List.of(Direction.WEST.delta().translate(point));
        case SLOPE_RIGHT -> List.of(Direction.EAST.delta().translate(point));
        case SLOPE_DOWN -> List.of(Direction.SOUTH.delta().translate(point));
        case SLOPE_UP -> List.of(Direction.NORTH.delta().translate(point));
      };
    }

    private Cell cellAt(Point point) {
      var y = (int) point.y();
      if (y < 0 || y >= cells.size()) {
        return Cell.WALL;
      }
      var line = cells.get(y);
      var x = (int) point.x();
      if (x < 0 || x >= line.size()) {
        return Cell.WALL;
      }

      return line.get(x);
    }

    private long dfs(Point start, Point end) {
      return dfs(start, end, Set.of(), 0);
    }

    private long dfs(Point start, Point end, Set<Point> discovered, long length) {
      if (start.equals(end)) {
        return length;
      }

      var max = 0L;
      for (var neighbour : neighboursOf(start)) {
        if (!discovered.contains(neighbour)) {
          var newDiscovered = new HashSet<>(discovered);
          newDiscovered.add(neighbour);
          max = Math.max(max, dfs(neighbour, end, newDiscovered, length + 1));
        }
      }
      return max;
    }

    private Point findStart() {
      var first = cells.getFirst();
      var pos =
          IntStream.range(0, cells.size())
              .filter(x -> first.get(x).equals(Cell.OPEN))
              .findFirst()
              .orElseThrow();
      return Point.of(pos, 0);
    }

    private Point findEnd() {
      var last = cells.getLast();
      var pos =
          IntStream.range(0, cells.size())
              .filter(x -> last.get(x).equals(Cell.OPEN))
              .findFirst()
              .orElseThrow();
      return Point.of(pos, cells.size() - 1);
    }
  }
}
