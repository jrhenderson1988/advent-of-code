package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
    return answer(patterns.getSummaryWithAlteredReflections());
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

    public Cell flip() {
      return switch (this) {
        case ASH -> ROCK;
        case ROCK -> ASH;
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

  private record ReflectionLine(int position, boolean horizontal) {
    public int getValue() {
      return horizontal ? 100 * position : position;
    }
  }

  private record Patterns(List<Grid> grids) {
    public static Patterns parse(String input) {
      return new Patterns(Lines.splitByEmptyLines(input.trim()).map(Grid::parse).toList());
    }

    public int getSummary() {
      return grids.stream()
          .map(Grid::findFirstReflectionLine)
          .map(ReflectionLine::getValue)
          .reduce(Integer::sum)
          .orElseThrow();
    }

    public int getSummaryWithAlteredReflections() {
      return grids.stream()
          .map(Grid::findAlternativeReflectionLineAfterFixingSmudge)
          .filter(Objects::nonNull)
          .map(ReflectionLine::getValue)
          .reduce(Integer::sum)
          .orElseThrow();
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

    private List<List<Cell>> flipCell(int x, int y) {
      var height = cells.size();
      var width = cells.getFirst().size();
      return IntStream.range(0, height)
          .mapToObj(
              i ->
                  IntStream.range(0, width)
                      .mapToObj(
                          j -> i == y && j == x ? cells.get(i).get(j).flip() : cells.get(i).get(j))
                      .toList())
          .toList();
    }

    public ReflectionLine findFirstReflectionLine() {
      return findReflectionLines().getFirst();
    }

    public ReflectionLine findAlternativeReflectionLineAfterFixingSmudge() {
      var first = findFirstReflectionLine();
      var height = cells.size();
      var width = cells.getFirst().size();

      for (var x = 0; x < width; x++) {
        for (var y = 0; y < height; y++) {
          var newCells = flipCell(x, y);
          var newGrid = new Grid(newCells);

          for (var rl : newGrid.findReflectionLines()) {
            if (!rl.equals(first)) {
              return rl;
            }
          }
        }
      }

      return null;
    }

    public List<ReflectionLine> findReflectionLines() {
      return Stream.of(
              findVerticalReflectionLines().stream(), findHorizontalReflectionLines().stream())
          .reduce(Stream::concat)
          .orElseThrow()
          .toList();
    }

    public List<ReflectionLine> findVerticalReflectionLines() {
      var width = cells.getFirst().size();
      var lines = new ArrayList<ReflectionLine>();
      for (var x = 0; x < width - 1; x++) {
        if (isReflectionAtColumn(x)) {
          lines.add(new ReflectionLine(x + 1, false));
        }
      }
      return lines;
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

    public List<ReflectionLine> findHorizontalReflectionLines() {
      var height = cells.size();
      var lines = new ArrayList<ReflectionLine>();
      for (var y = 0; y < height - 1; y++) {
        if (isReflectionAtRow(y)) {
          lines.add(new ReflectionLine(y + 1, true));
        }
      }
      return lines;
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
