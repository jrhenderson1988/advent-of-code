package uk.co.jonathonhenderson.aoc.days;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import uk.co.jonathonhenderson.aoc.common.Lines;

public class Day13 extends Day {

  private final Patterns patterns;

  public Day13(String input) {
    this.patterns = Patterns.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(patterns.getSummary());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private enum Cell {
    ASH,
    ROCK;

    public static Cell of(char ch) {
      return switch (ch) {
        case '.' -> ASH;
        case '#' -> ROCK;
        default -> throw new IllegalArgumentException("Unknown cell");
      };
    }

    @Override
    public String toString() {
      return switch (this) {
        case ASH -> ".";
        case ROCK -> "#";
      };
    }
  }

  private record Patterns(List<Grid> grids) {
    public static Patterns parse(String input) {
      return new Patterns(Lines.splitByEmptyLines(input.trim()).map(Grid::parse).toList());
    }

    public int getSummary() {
      var colSum =
          grids.stream()
              .map(Grid::totalColumnsBeforeReflectionLine)
              .reduce(Integer::sum)
              .orElseThrow();

      var rowSum =
          grids.stream().map(Grid::totalRowsAboveReflectionLine).reduce(Integer::sum).orElseThrow()
              * 100;

      return colSum + rowSum;
    }

    @Override
    public String toString() {
      return grids.stream().map(Grid::toString).collect(Collectors.joining("\n\n"));
    }
  }

  private record Grid(List<List<Cell>> cells) {
    public static Grid parse(String input) {
      return new Grid(
          input
              .trim()
              .lines()
              .map(line -> line.trim().chars().mapToObj(ch -> Cell.of((char) ch)).toList())
              .toList());
    }

    public int totalColumnsBeforeReflectionLine() {
      var width = cells.getFirst().size();
      for (var x = 0; x < width - 1; x++) {
        if (isReflectionAtColumn(x)) {
          return x + 1;
        }
      }
      return 0;
    }

    private boolean isReflectionAtColumn(int x) {
      var width = cells.getFirst().size();
      for (var i = 0; i < width; i++) {
        var prevIdx = x - i;
        var nextIdx = x + i + 1;
        if (prevIdx < 0 || nextIdx >= width) {
          break;
        }
        var prevCol = getColumn(prevIdx);
        var nextCol = getColumn(nextIdx);
        if (!prevCol.equals(nextCol)) {
          return false;
        }
      }
      return true;
    }

    public int totalRowsAboveReflectionLine() {
      var height = cells.size();
      for (var y = 0; y < height - 1; y++) {
        if (isReflectionAtRow(y)) {
          return y + 1;
        }
      }
      return 0;
    }

    private boolean isReflectionAtRow(int y) {
      var height = cells.size();
      for (var i = 0; i < height; i++) {
        var prevIdx = y - i;
        var nextIdx = y + i + 1;
        if (prevIdx < 0 || nextIdx >= height) {
          break;
        }
        var prevRow = getRow(prevIdx);
        var nextRow = getRow(nextIdx);
        if (!prevRow.equals(nextRow)) {
          return false;
        }
      }
      return true;
    }

    private List<Cell> getColumn(int x) {
      return cells.stream().map(row -> row.get(x)).toList();
    }

    private List<Cell> getRow(int y) {
      return cells.get(y);
    }

    @Override
    public String toString() {
      return cells.stream()
          .map(row -> row.stream().map(Cell::toString).collect(Collectors.joining()))
          .collect(Collectors.joining("\n"));
    }
  }
}
