package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Pair;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day10 extends Day {

  private final Grid grid;

  public Day10(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(grid.findDistanceOfFurthestPipe());
  }

  @Override
  public Optional<String> part2() {
    return answer(grid.totalTilesWithinLoop());
  }

  private enum Tile {
    VERTICAL,
    HORIZONTAL,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_LEFT,
    TOP_RIGHT;

    private static final Set<Tile> MOVING_UP = Set.of(VERTICAL, TOP_LEFT, TOP_RIGHT);
    private static final Set<Tile> MOVING_DOWN = Set.of(VERTICAL, BOTTOM_LEFT, BOTTOM_RIGHT);
    private static final Set<Tile> MOVING_LEFT = Set.of(HORIZONTAL, TOP_LEFT, BOTTOM_LEFT);
    private static final Set<Tile> MOVING_RIGHT = Set.of(HORIZONTAL, TOP_RIGHT, BOTTOM_RIGHT);

    public static Tile of(char ch) {
      return switch (ch) {
        case '|' -> VERTICAL;
        case '-' -> HORIZONTAL;
        case 'L' -> BOTTOM_LEFT;
        case 'J' -> BOTTOM_RIGHT;
        case 'F' -> TOP_LEFT;
        case '7' -> TOP_RIGHT;
        default -> throw new IllegalStateException("Unexpected value: " + ch);
      };
    }

    public Map<Point, Set<Tile>> deltas() {
      return switch (this) {
        case VERTICAL -> Map.of(Point.of(0, -1), MOVING_UP, Point.of(0, 1), MOVING_DOWN);
        case HORIZONTAL -> Map.of(Point.of(-1, 0), MOVING_LEFT, Point.of(1, 0), MOVING_RIGHT);
        case BOTTOM_LEFT -> Map.of(Point.of(0, -1), MOVING_UP, Point.of(1, 0), MOVING_RIGHT);
        case BOTTOM_RIGHT -> Map.of(Point.of(-1, 0), MOVING_LEFT, Point.of(0, -1), MOVING_UP);
        case TOP_LEFT -> Map.of(Point.of(0, 1), MOVING_DOWN, Point.of(1, 0), MOVING_RIGHT);
        case TOP_RIGHT -> Map.of(Point.of(-1, 0), MOVING_LEFT, Point.of(0, 1), MOVING_DOWN);
      };
    }

    public Map<Point, Set<Tile>> deltas(Point from) {
      return deltas().entrySet().stream()
          .map(e -> Pair.of(e.getKey().translate(from), e.getValue()))
          .collect(Collectors.toMap(Pair::first, Pair::second));
    }
  }

  private record Grid(Map<Point, Tile> pipes, Point start, int width, int height) {
    private static final Set<Tile> INCLUSIVE_VERTICAL_TILES =
        Set.of(Tile.VERTICAL, Tile.TOP_LEFT, Tile.TOP_RIGHT);

    public static Grid parse(String input) {
      var lines = input.trim().lines().toList();
      if (lines.isEmpty()) {
        throw new IllegalArgumentException("Invalid input");
      }

      var pipes = new HashMap<Point, Tile>();
      var height = lines.size();
      var width = lines.getFirst().trim().length();

      Point start = null;
      for (var y = 0; y < lines.size(); y++) {
        var line = lines.get(y).trim().toCharArray();
        for (var x = 0; x < line.length; x++) {
          var ch = line[x];
          if (ch == 'S') {
            start = Point.of(x, y);
          } else if (ch != '.') {
            var point = Point.of(x, y);
            var tile = Tile.of(ch);
            pipes.put(point, tile);
          }
        }
      }

      pipes.put(start, findStartingTile(pipes, start));

      return new Grid(pipes, start, width, height);
    }

    private static Tile findStartingTile(Map<Point, Tile> pipes, Point start) {
      var possibilities =
          Stream.of(Tile.values()).filter(t -> isPossibleStartingTile(pipes, start, t)).toList();
      if (possibilities.size() != 1) {
        throw new IllegalArgumentException("Starting tile can't be determined");
      }

      return possibilities.getFirst();
    }

    private static boolean isPossibleStartingTile(Map<Point, Tile> p, Point start, Tile t) {
      return t.deltas(start).entrySet().stream()
          .allMatch(e -> p.containsKey(e.getKey()) && e.getValue().contains(p.get(e.getKey())));
    }

    public int findDistanceOfFurthestPipe() {
      return walkLoop().size() / 2;
    }

    private Map<Point, Tile> walkLoop() {
      var currentDelta = pipes.get(start).deltas().keySet().stream().findFirst().orElseThrow();
      var current = start.translate(currentDelta);

      var actualLoop = new HashMap<Point, Tile>();
      actualLoop.put(start, pipes.get(start));
      while (!current.equals(start)) {
        var tile = pipes.get(current);
        actualLoop.put(current, tile);
        var flipped = currentDelta.flip();
        var deltas = tile.deltas();
        var nextDeltas = deltas.keySet().stream().filter(d -> !d.equals(flipped)).toList();
        if (nextDeltas.size() != 1) {
          throw new IllegalArgumentException("unexpected deltas");
        }
        var nextDelta = nextDeltas.getFirst();
        current = current.translate(nextDelta);
        currentDelta = nextDelta;
      }

      return actualLoop;
    }

    public int totalTilesWithinLoop() {
      var actualLoop = walkLoop();

      return (int)
          IntStream.range(0, width)
              .mapToObj(x -> IntStream.range(0, height).mapToObj(y -> Point.of(x, y)))
              .reduce(Stream::concat)
              .orElseThrow()
              .filter(pt -> !actualLoop.containsKey(pt))
              .filter(pt -> isTileInside(actualLoop, pt))
              .count();
    }

    private boolean isTileInside(Map<Point, Tile> pipes, Point point) {
      return countInclusiveVerticalTilesInRow(pipes, point) % 2 == 1;
    }

    private int countInclusiveVerticalTilesInRow(Map<Point, Tile> pipes, Point point) {
      return (int)
          IntStream.range((int) point.x(), width)
              .mapToObj(x -> Point.of(x, point.y()))
              .filter(pipes::containsKey)
              .filter(pt -> INCLUSIVE_VERTICAL_TILES.contains(pipes.get(pt)))
              .count();
    }
  }
}
