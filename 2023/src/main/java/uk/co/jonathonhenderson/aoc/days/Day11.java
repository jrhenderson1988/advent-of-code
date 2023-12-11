package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day11 extends Day {

  private final Galaxy galaxy;

  public Day11(String input) {
    this.galaxy = Galaxy.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(galaxy.expand().sumOfDistancesBetweenAllPoints());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private enum Cell {
    GALAXY,
    SPACE;

    public static Cell of(char ch) {
      return switch (ch) {
        case '#' -> GALAXY;
        case '.' -> SPACE;
        default -> throw new IllegalArgumentException("Unexpected cell type");
      };
    }
  }

  private record Galaxy(List<List<Cell>> cells) {
    public static Galaxy parse(String input) {
      var cells =
          input
              .trim()
              .lines()
              .map(line -> line.trim().chars().mapToObj(ch -> Cell.of((char) ch)).toList())
              .toList();

      return new Galaxy(cells);
    }

    public Galaxy expand() {
      var emptyCols = findEmptyColumns();
      var emptyRows = findEmptyRows();
      var newCells = new ArrayList<List<Cell>>();

      for (var y = 0; y < height(); y++) {
        var row = new ArrayList<Cell>();
        for (var x = 0; x < width(); x++) {
          row.add(cellAt(x, y));
          if (emptyCols.contains(x)) {
            row.add(cellAt(x, y));
          }
        }
        newCells.add(row);
        if (emptyRows.contains(y)) {
          newCells.add(row);
        }
      }

      // System.out.println("Empty rows: %s, empty cols: %s".formatted(emptyRows, emptyCols));
      return new Galaxy(newCells);
    }

    private List<Integer> findEmptyColumns() {
      return IntStream.range(0, width())
          .filter(
              x ->
                  IntStream.range(0, height())
                      .mapToObj(y -> cellAt(x, y))
                      .allMatch(cell -> cell.equals(Cell.SPACE)))
          .boxed()
          .toList();
    }

    private List<Integer> findEmptyRows() {
      return IntStream.range(0, height())
          .filter(
              y ->
                  IntStream.range(0, width())
                      .mapToObj(x -> cellAt(x, y))
                      .allMatch(cell -> cell.equals(Cell.SPACE)))
          .boxed()
          .toList();
    }

    public int sumOfDistancesBetweenAllPoints() {
      var allGalaxies =
          IntStream.range(0, width())
              .mapToObj(x -> IntStream.range(0, height()).mapToObj(y -> Point.of(x, y)))
              .reduce(Stream::concat)
              .orElseThrow()
              .filter(pt -> cellAt(pt.x(), pt.y()).equals(Cell.GALAXY))
              .toList();

      var sum = 0;
      for (var a = 0; a < allGalaxies.size(); a++) {
        var pointA = allGalaxies.get(a);
        for (var b = a + 1; b < allGalaxies.size(); b++) {
          var pointB = allGalaxies.get(b);
//          System.out.println("Dist (%s-> %s) = %d".formatted(pointA, pointB, pointA.manhattanDistanceTo(pointB)));
          sum += pointA.manhattanDistanceTo(pointB);
        }
      }

      return sum;
    }

    private Cell cellAt(int x, int y) {
      return cells.get(y).get(x);
    }

    private int width() {
      return cells.getFirst().size();
    }

    private int height() {
      return cells.size();
    }
  }
}
