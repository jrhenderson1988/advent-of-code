package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import uk.co.jonathonhenderson.aoc.common.Direction;

public class Day14 extends Day {

  private final Grid grid;

  public Day14(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(grid.totalLoadOnNorthSide());
  }

  @Override
  public Optional<String> part2() {
    return answer(grid.spinCycle(1000000000).calculateLoad());
  }

  private enum Cell {
    ROUND_ROCK,
    CUBE_ROCK,
    SPACE;

    public static Cell of(char ch) {
      return switch (ch) {
        case '.' -> SPACE;
        case 'O' -> ROUND_ROCK;
        case '#' -> CUBE_ROCK;
        default -> throw new IllegalArgumentException("Unrecognised cell type");
      };
    }

    @Override
    public String toString() {
      return switch (this) {
        case ROUND_ROCK -> "O";
        case CUBE_ROCK -> "#";
        case SPACE -> ".";
      };
    }
  }

  private record Grid(List<List<Cell>> cells) {
    public static Grid parse(String input) {
      var cells = input.trim().lines().map(Grid::parseLine).toList();
      return new Grid(cells);
    }

    private static List<Cell> parseLine(String line) {
      return line.trim().chars().mapToObj(ch -> Cell.of((char) ch)).toList();
    }

    public int totalLoadOnNorthSide() {
      return tilt(Direction.NORTH).calculateLoad();
    }

    public Grid spinCycle(long times) {
      var grid = this;

      var seen = new HashMap<Grid, Long>();
      for (var i = 0L; i < times; i++) {
        grid = grid.spinCycle();
        if (seen.containsKey(grid)) {
          return shortCircuit(times, grid, i);
        }
        seen.put(grid, i);
      }
      return grid;
    }

    private Grid shortCircuit(long times, Grid grid, long pos) {
      var current = grid;

      var every = 1;
      while (every < times) {
        current = current.spinCycle();
        if (current.equals(grid)) {
          break;
        }
        every++;
      }

      var newTimes = times - pos;
      var newPos = newTimes - (newTimes % every);
      var iterationsLeft = newTimes - newPos - 1;

      current = grid;
      for (var i = 0L; i < iterationsLeft; i++) {
        current = current.spinCycle();
      }

      return current;
    }

    public Grid spinCycle() {
      return tilt(Direction.NORTH).tilt(Direction.WEST).tilt(Direction.SOUTH).tilt(Direction.EAST);
    }

    public Grid tilt(Direction direction) {
      var height = cells.size();
      var width = cells.getFirst().size();
      var oppositeDelta = direction.opposite().delta();

      var newCells =
          new ArrayList<>(
              cells.stream().map(row -> new ArrayList<>(row.stream().toList())).toList());
      while (true) {
        var movements = false;
        for (var y = 0; y < height; y++) {
          for (var x = 0; x < width; x++) {
            var curr = newCells.get(y).get(x);
            if (curr.equals(Cell.SPACE)) {
              var nextY = (int) oppositeDelta.y() + y;
              var nextX = (int) oppositeDelta.x() + x;
              if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) {
                continue;
              }

              var next = newCells.get(nextY).get(nextX);
              if (next.equals(Cell.ROUND_ROCK)) {
                var currRow = newCells.get(y);
                currRow.set(x, Cell.ROUND_ROCK);
                newCells.set(y, currRow);

                var nextRow = newCells.get(nextY);
                nextRow.set(nextX, Cell.SPACE);
                newCells.set(nextY, nextRow);

                movements = true;
              }
            }
          }
        }

        if (!movements) {
          return new Grid(newCells.stream().map(row -> row.stream().toList()).toList());
        }
      }
    }

    public int calculateLoad() {
      var height = cells.size();
      var width = cells.getFirst().size();

      var total = 0;
      for (var y = 0; y < height; y++) {
        for (var x = 0; x < width; x++) {
          total +=
              switch (cells.get(y).get(x)) {
                case ROUND_ROCK -> height - y;
                case CUBE_ROCK, SPACE -> 0;
              };
        }
      }

      return total;
    }

    @Override
    public String toString() {
      return cells.stream()
          .map(row -> row.stream().map(Enum::toString).collect(Collectors.joining()))
          .collect(Collectors.joining("\n"));
    }
  }
}
