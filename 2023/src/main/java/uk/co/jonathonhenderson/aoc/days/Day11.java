package uk.co.jonathonhenderson.aoc.days;

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
    return answer(galaxy.expand(2).sumOfDistancesBetweenAllPoints());
  }

  public Optional<String> part2(int expansionFactor) {
    return answer(galaxy.expand(expansionFactor).sumOfDistancesBetweenAllPoints());
  }

  @Override
  public Optional<String> part2() {
    return part2(1000000);
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

  private record Galaxy(List<List<Cell>> cells, int expansionFactor) {
    public static Galaxy parse(String input) {
      var cells =
          input
              .trim()
              .lines()
              .map(line -> line.trim().chars().mapToObj(ch -> Cell.of((char) ch)).toList())
              .toList();

      return new Galaxy(cells, 1);
    }

    public Galaxy expand(int expansionFactor) {
      return new Galaxy(cells, expansionFactor);
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

    public long sumOfDistancesBetweenAllPoints() {
      var emptyCols = findEmptyColumns();
      var emptyRows = findEmptyRows();

      var allGalaxies =
          IntStream.range(0, width())
              .mapToObj(x -> IntStream.range(0, height()).mapToObj(y -> Point.of(x, y)))
              .reduce(Stream::concat)
              .orElseThrow()
              .filter(pt -> cellAt(pt.x(), pt.y()).equals(Cell.GALAXY))
              .toList();

      var sum = 0L;
      for (var a = 0; a < allGalaxies.size(); a++) {
        var pointA = allGalaxies.get(a);
        for (var b = a + 1; b < allGalaxies.size(); b++) {
          var pointB = allGalaxies.get(b);
          sum += distanceBetween(pointA, pointB, emptyRows, emptyCols);
        }
      }

      return sum;
    }

    private long distanceBetween(
        Point a, Point b, List<Integer> emptyRows, List<Integer> emptyCols) {
      var crossedEmptyCols =
          (int)
              emptyCols.stream()
                  .filter(col -> col >= Math.min(a.x(), b.x()))
                  .filter(col -> col <= Math.max(a.x(), b.x()))
                  .count();
      var crossedEmptyRows =
          (int)
              emptyRows.stream()
                  .filter(row -> row >= Math.min(a.y(), b.y()))
                  .filter(row -> row <= Math.max(a.y(), b.y()))
                  .count();

      var xDist =
          (Math.abs(a.x() - b.x()) - crossedEmptyCols) + (expansionFactor * crossedEmptyCols);
      var yDist =
          (Math.abs(a.y() - b.y()) - crossedEmptyRows) + (expansionFactor * crossedEmptyRows);

      return xDist + yDist;
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
