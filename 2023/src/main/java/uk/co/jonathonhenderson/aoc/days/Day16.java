package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day16 extends Day {

  private final Grid grid;

  public Day16(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(grid.totalEnergisedCells(Point.of(0, 0), Direction.EAST));
  }

  @Override
  public Optional<String> part2() {
    return answer(grid.maximumEnergisedCells());
  }

  private enum Cell {
    SPACE,
    FORWARD_MIRROR,
    BACKWARD_MIRROR,
    HORIZONTAL_SPLITTER,
    VERTICAL_SPLITTER;

    public static Cell of(char ch) {
      return switch (ch) {
        case '.' -> SPACE;
        case '/' -> FORWARD_MIRROR;
        case '\\' -> BACKWARD_MIRROR;
        case '-' -> HORIZONTAL_SPLITTER;
        case '|' -> VERTICAL_SPLITTER;
        default -> throw new IllegalArgumentException("Invalid cell");
      };
    }
  }

  private record State(Point position, Direction direction) {
    public static State of(Point position, Direction direction) {
      return new State(position, direction);
    }
  }

  private record Grid(Map<Point, Cell> cells) {
    public static Grid parse(String input) {
      var cells = new HashMap<Point, Cell>();
      var lines = input.trim().lines().toList();
      for (var y = 0; y < lines.size(); y++) {
        var line = lines.get(y).trim().toCharArray();
        for (var x = 0; x < line.length; x++) {
          cells.put(Point.of(x, y), Cell.of(line[x]));
        }
      }
      return new Grid(cells);
    }

    public int totalEnergisedCells(Point start, Direction direction) {
      return tracePath(start, direction).size();
    }

    private long getWidth() {
      return cells.keySet().stream().map(Point::x).reduce(Long::max).orElseThrow();
    }

    private long getHeight() {
      return cells.keySet().stream().map(Point::y).reduce(Long::max).orElseThrow();
    }

    public int maximumEnergisedCells() {
      var width = getWidth();
      var height = getHeight();

      return Stream.of(
              LongStream.range(0, width).mapToObj(x -> State.of(Point.of(x, 0), Direction.SOUTH)),
              LongStream.range(0, height).mapToObj(y -> State.of(Point.of(0, y), Direction.EAST)),
              LongStream.range(0, width)
                  .mapToObj(x -> State.of(Point.of(x, height - 1), Direction.NORTH)),
              LongStream.range(0, height)
                  .mapToObj(y -> State.of(Point.of(width - 1, y), Direction.WEST)))
          .reduce(Stream::concat)
          .orElseThrow()
          .map(s -> totalEnergisedCells(s.position(), s.direction()))
          .reduce(Integer::max)
          .orElseThrow();
    }

    private Set<Point> tracePath(Point start, Direction initialDirection) {
      var states = List.of(State.of(start, initialDirection));
      var occupied = new HashSet<>(List.of(start));
      var seen = new HashSet<>(states);
      while (!states.isEmpty()) {
        states =
            states.stream()
                .map(this::apply)
                .flatMap(List::stream)
                .filter(state -> !seen.contains(state))
                .toList();
        states.forEach(
            s -> {
              occupied.add(s.position());
              seen.add(s);
            });
      }
      return occupied;
    }

    private List<State> apply(State state) {
      var cell = cells.get(state.position());
      return switch (cell) {
        case SPACE -> passThrough(state.position(), state.direction());
        case FORWARD_MIRROR -> passThrough(
            state.position(),
            switch (state.direction()) {
              case NORTH -> Direction.EAST;
              case EAST -> Direction.NORTH;
              case SOUTH -> Direction.WEST;
              case WEST -> Direction.SOUTH;
            });
        case BACKWARD_MIRROR -> passThrough(
            state.position(),
            switch (state.direction()) {
              case NORTH -> Direction.WEST;
              case EAST -> Direction.SOUTH;
              case SOUTH -> Direction.EAST;
              case WEST -> Direction.NORTH;
            });
        case HORIZONTAL_SPLITTER -> switch (state.direction()) {
          case EAST, WEST -> passThrough(state.position(), state.direction());
          case NORTH, SOUTH -> splitHorizontal(state);
        };
        case VERTICAL_SPLITTER -> switch (state.direction()) {
          case NORTH, SOUTH -> passThrough(state.position(), state.direction());
          case EAST, WEST -> splitVertical(state);
        };
      };
    }

    private List<State> passThrough(Point position, Direction direction) {
      var newPosition = position.translate(direction.delta());
      if (cells.containsKey(newPosition)) {
        return List.of(State.of(newPosition, direction));
      } else {
        return List.of();
      }
    }

    private List<State> splitHorizontal(State state) {
      var west = passThrough(state.position(), Direction.WEST);
      var east = passThrough(state.position(), Direction.EAST);
      return Stream.of(west, east).flatMap(List::stream).toList();
    }

    private List<State> splitVertical(State state) {
      var north = passThrough(state.position(), Direction.NORTH);
      var south = passThrough(state.position(), Direction.SOUTH);
      return Stream.of(north, south).flatMap(List::stream).toList();
    }
  }
}
