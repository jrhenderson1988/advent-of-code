package uk.co.jonathonhenderson.aoc.days;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day21 extends Day {

  private final Grid grid;

  public Day21(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return part1(64);
  }

  public Optional<String> part1(int steps) {
    return answer(grid.totalGardensPossibleInSteps(steps));
  }

  @Override
  public Optional<String> part2() {
    // this was a struggle - needed to do a lot of reading to understand the maths
    return part2(26501365);
  }

  public Optional<String> part2(int steps) {
    return answer(grid.totalGardensPossibleWithInfiniteMapInSteps(steps));
  }

  private enum Cell {
    GARDEN,
    ROCK;

    public static Cell of(char ch) {
      return switch (ch) {
        case '.', 'S' -> GARDEN;
        case '#' -> ROCK;
        default -> throw new IllegalArgumentException("Unexpected cell " + ch);
      };
    }
  }

  private record CellPoint(Point cell, Point point) {
    public static CellPoint of(Point cell, Point point) {
      return new CellPoint(cell, point);
    }

    public static CellPoint of(long cellX, long cellY, long pointX, long pointY) {
      return of(Point.of(cellX, cellY), Point.of(pointX, pointY));
    }

    public CellPoint translate(Point delta, long gridSize) {
      var translated = point.translate(delta);
      if (translated.x() < 0) {
        return CellPoint.of(cell.x() - 1, cell.y(), gridSize - 1, translated.y());
      } else if (translated.x() >= gridSize) {
        return CellPoint.of(cell.x() + 1, cell.y(), 0, translated.y());
      } else if (translated.y() < 0) {
        return CellPoint.of(cell.x(), cell.y() - 1, translated.x(), gridSize - 1);
      } else if (translated.y() >= gridSize) {
        return CellPoint.of(cell.x(), cell.y() + 1, translated.x(), 0);
      } else {
        return CellPoint.of(cell.x(), cell.y(), translated.x(), translated.y());
      }
    }
  }

  private record Grid(Map<Point, Cell> cells, Point start, String input) {
    public static Grid parse(String input) {
      var lines = input.trim().lines().toList();

      var cells =
          IntStream.range(0, lines.size())
              .mapToObj(
                  y -> IntStream.range(0, lines.get(y).length()).mapToObj(x -> Point.of(x, y)))
              .reduce(Stream::concat)
              .orElseThrow()
              .collect(
                  Collectors.toMap(
                      pt -> pt, pt -> Cell.of(lines.get((int) pt.y()).charAt((int) pt.x()))));
      var start =
          IntStream.range(0, lines.size())
              .mapToObj(
                  y -> IntStream.range(0, lines.get(y).length()).mapToObj(x -> Point.of(x, y)))
              .reduce(Stream::concat)
              .orElseThrow()
              .filter(pt -> lines.get((int) pt.y()).charAt((int) pt.x()) == 'S')
              .findFirst()
              .orElseThrow();

      return new Grid(cells, start, input);
    }

    public int totalGardensPossibleInSteps(int steps) {
      return explore(Set.of(start), steps).size();
    }

    private Set<Point> explore(Set<Point> from, int stepsRemaining) {
      var positions = new HashSet<>(from);
      for (var i = 0; i < stepsRemaining; i++) {
        var newPositions = new HashSet<Point>();
        for (var pos : positions) {
          var neighbours = possibleNeighboursOf(pos);
          newPositions.addAll(neighbours);
        }
        positions = newPositions;
      }

      return positions;
    }

    private List<Point> possibleNeighboursOf(Point from) {
      return Arrays.stream(Direction.values())
          .map(d -> d.delta().translate(from))
          .filter(p -> Cell.GARDEN.equals(cells.get(p)))
          .toList();
    }

    public long totalGardensPossibleWithInfiniteMapInSteps(long steps) {
      var width = getWidth(); // 131
      var height = getHeight(); // 131
      if (width != height) {
        throw new IllegalArgumentException("Grid is not a square");
      }
      var firstSteps = steps % width; // 65
      var secondSteps = width + firstSteps; // 196
      var thirdSteps = (width * 2) + firstSteps; // 327
      var initial = Set.of(CellPoint.of(Point.of(0, 0), start));
      var first = infiniteExplore(initial, firstSteps, width).size();
      var second = infiniteExplore(initial, secondSteps, width).size();
      var third = infiniteExplore(initial, thirdSteps, width).size();
      var n = steps / width;

      return first + n * (second - first + (n - 1) * (third - second - second + first) / 2);
    }

    private long getWidth() {
      return cells.keySet().stream().map(Point::x).reduce(Long::max).orElseThrow() + 1;
    }

    private long getHeight() {
      return cells.keySet().stream().map(Point::y).reduce(Long::max).orElseThrow() + 1;
    }

    private Set<CellPoint> infiniteExplore(
        Set<CellPoint> from, long stepsRemaining, long gridSize) {
      var positions = new HashSet<>(from);
      for (var i = 0; i < stepsRemaining; i++) {
        var newPositions = new HashSet<CellPoint>();
        for (var pos : positions) {
          var neighbours = possibleNeighboursOf(pos, gridSize);
          newPositions.addAll(neighbours);
        }
        positions = newPositions;
      }

      return positions;
    }

    private List<CellPoint> possibleNeighboursOf(CellPoint from, long gridSize) {
      return Arrays.stream(Direction.values())
          .map(d -> from.translate(d.delta(), gridSize))
          .filter(cp -> Cell.GARDEN.equals(cells.get(cp.point())))
          .toList();
    }
  }
}
