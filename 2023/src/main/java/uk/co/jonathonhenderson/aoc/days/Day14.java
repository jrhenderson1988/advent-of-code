package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day14 extends Day {

  private final Grid grid;

  public Day14(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    // System.out.println(grid);
    // System.out.println("========");
    // System.out.println(grid.tilt());
    return answer(grid.totalLoadOnNorthSide());
  }

  @Override
  public Optional<String> part2() {
    return answer();
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
      return tilt().calculateLoad();
    }

    public Grid tilt() {
      var height = cells.size();
      var width = cells.getFirst().size();

      var newCells =
          new ArrayList<>(
              cells.stream().map(row -> new ArrayList<>(row.stream().toList())).toList());
      while (true) {
        var movements = false;
        for (var y = 0; y < height - 1; y++) {
          for (var x = 0; x < width; x++) {
            var curr = newCells.get(y).get(x);
            if (curr.equals(Cell.SPACE)) {
              var under = newCells.get(y + 1).get(x);
              if (under.equals(Cell.ROUND_ROCK)) {
                var currRow = newCells.get(y);
                currRow.set(x, Cell.ROUND_ROCK);
                newCells.set(y, currRow);
                var nextRow = newCells.get(y + 1);
                nextRow.set(x, Cell.SPACE);
                newCells.set(y + 1, nextRow);

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
