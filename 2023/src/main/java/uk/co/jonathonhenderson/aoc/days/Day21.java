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

  private record Grid(Map<Point, Cell> cells, Point start) {
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
      return new Grid(cells, start);
    }

    public long totalGardensPossibleWithInfiniteMapInSteps(int steps) {
      // TODO
      return 0;
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
  }
}