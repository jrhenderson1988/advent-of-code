package uk.co.jonathonhenderson.aoc.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Pair;
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
    return answer(grid.lengthOfLongestHikeIgnoringHills());
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

  private static class Grid {
    private final List<List<Cell>> cells;
    private final Point start;
    private final Point end;
    private final Map<Point, List<Pair<Point, Integer>>> neighbourCache;

    public Grid(List<List<Cell>> cells) {
      this.cells = cells;
      this.start = findStart();
      this.end = findEnd();
      this.neighbourCache = new HashMap<>();
    }

    public static Grid parse(String input) {
      return new Grid(
          input.trim().lines().toList().stream()
              .map(line -> line.trim().chars().mapToObj(ch -> Cell.of((char) ch)).toList())
              .toList());
    }

    public long lengthOfLongestHike() {
      var start = findStart();
      var end = findEnd();

      return dfs(start, end, this::neighboursOf);
    }

    public long lengthOfLongestHikeIgnoringHills() {
      var start = findStart();
      var end = findEnd();

      return dfs(start, end, this::neighboursOfIgnoringHills);
    }

    private List<Pair<Point, Integer>> neighboursOf(Point point) {
      return switch (cellAt(point)) {
        case OPEN -> Arrays.stream(Direction.values())
            .map(d -> d.delta().translate(point))
            .filter(p -> !cellAt(p).equals(Cell.WALL))
            .map(p -> Pair.of(p, 1))
            .toList();
        case WALL -> throw new IllegalStateException("Should not be reachable");
        case SLOPE_LEFT -> List.of(Pair.of(Direction.WEST.delta().translate(point), 1));
        case SLOPE_RIGHT -> List.of(Pair.of(Direction.EAST.delta().translate(point), 1));
        case SLOPE_DOWN -> List.of(Pair.of(Direction.SOUTH.delta().translate(point), 1));
        case SLOPE_UP -> List.of(Pair.of(Direction.NORTH.delta().translate(point), 1));
      };
    }

    public List<Pair<Point, Integer>> neighboursOfIgnoringHills(Point point) {
      if (cellAt(point).equals(Cell.WALL)) {
        throw new IllegalStateException("Should not be reachable");
      }

      return neighbourCache.computeIfAbsent(
          point,
          (pt) -> {
            var directions = getPossibleDirections(pt);
            return directions.size() > 2
                ? directions.stream().map(d -> Pair.of(d.delta().translate(pt), 1)).toList()
                : directions.stream().map(dir -> seekNeighbour(pt, dir)).toList();
          });
    }

    private List<Direction> getPossibleDirections(Point point) {
      return Arrays.stream(Direction.values())
          .filter(d -> !cellAt(d.delta().translate(point)).equals(Cell.WALL))
          .toList();
    }

    private Pair<Point, Integer> seekNeighbour(Point point, Direction direction) {
      var currentDirection = direction;
      var currentPoint = point;
      var steps = 0;

      while (true) {
        steps++;
        currentPoint = currentDirection.delta().translate(currentPoint);

        var possibleDirections = getPossibleDirections(currentPoint);
        if (currentPoint.equals(start)) {
          return Pair.of(start, steps);
        } else if (currentPoint.equals(end)) {
          return Pair.of(end, steps);
        } else if (possibleDirections.size() > 2) {
          return Pair.of(currentPoint, steps);
        } else if (possibleDirections.size() == 1) {
          throw new IllegalStateException("Should not be reachable");
        }

        var first = possibleDirections.getFirst();
        var second = possibleDirections.getLast();
        if (first.equals(currentDirection.opposite())) {
          currentDirection = second;
        } else {
          currentDirection = first;
        }
      }
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

    private long dfs(
        Point start, Point end, Function<Point, List<Pair<Point, Integer>>> neighbourFn) {
      return dfs(start, end, Set.of(), 0, neighbourFn);
    }

    private long dfs(
        Point start,
        Point end,
        Set<Point> discovered,
        long length,
        Function<Point, List<Pair<Point, Integer>>> neighbourFn) {
      if (start.equals(end)) {
        return length;
      }

      var max = 0L;
      var neighbourPairs = neighbourFn.apply(start);
      for (var neighbourPair : neighbourPairs) {
        var neighbour = neighbourPair.first();
        var cost = neighbourPair.second();
        if (!discovered.contains(neighbour)) {
          max =
              Math.max(
                  max,
                  dfs(neighbour, end, discover(discovered, neighbour), length + cost, neighbourFn));
        }
      }
      return max;
    }

    private Set<Point> discover(Set<Point> discovered, Point point) {
      var newDiscovered = new HashSet<>(discovered);
      newDiscovered.add(point);
      return newDiscovered;
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
