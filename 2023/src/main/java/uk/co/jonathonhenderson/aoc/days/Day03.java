package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day03 extends Day {

  private final Schematic schematic;

  public Day03(String input) {
    this.schematic = Schematic.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(schematic.sumPartNumbers());
  }

  @Override
  public Optional<String> part2() {
    return answer(schematic.sumGearRatios());
  }

  private sealed interface Cell {
    static Cell parse(char c) {
      if (Character.isDigit(c)) {
        return new Digit(Integer.parseInt("" + c));
      } else if (c != '.') {
        return new Symbol(c);
      } else {
        return null;
      }
    }

    record Digit(int digit) implements Cell {}

    record Symbol(char symbol) implements Cell {}
  }

  private record PartNumber(List<Point> points, int value) {}

  private record Schematic(Map<Point, Cell> grid, int width, int height) {
    public static Schematic parse(String input) {
      var grid = new HashMap<Point, Cell>();
      var lines = input.trim().lines().toList();
      var width = 0;
      for (var y = 0; y < lines.size(); y++) {
        var line = lines.get(y);
        var chars = line.toCharArray();
        width = Math.max(chars.length, width);
        for (var x = 0; x < chars.length; x++) {
          var cell = Cell.parse(chars[x]);
          if (cell != null) {
            grid.put(Point.of(x, y), cell);
          }
        }
      }

      return new Schematic(grid, width, lines.size());
    }

    public int sumPartNumbers() {
      return findAllPartNumbers().stream()
          .map(PartNumber::value)
          .reduce(Integer::sum)
          .orElseThrow();
    }

    private List<PartNumber> findAllPartNumbers() {
      return IntStream.range(0, height)
          .mapToObj(this::findAllPartNumbersInLine)
          .reduce((a, b) -> Stream.concat(a.stream(), b.stream()).toList())
          .orElseThrow();
    }

    private List<PartNumber> findAllPartNumbersInLine(int y) {
      return IntStream.range(0, width)
          .mapToObj(x -> Point.of(x, y))
          .filter(this::isDigit)
          .filter(this::isFirstDigit)
          .map(this::getDigitPointsFrom)
          .filter(this::isPartNumber)
          .map(this::toPartNumber)
          .toList();
    }

    private PartNumber toPartNumber(List<Point> points) {
      return new PartNumber(points, toNumber(points));
    }

    private int toNumber(List<Point> points) {
      var total = 0;
      var multiplier = 1;
      for (var i = points.size() - 1; i >= 0; i--) {
        var point = points.get(i);
        var digitCell = (Cell.Digit) grid.get(point);
        total += (multiplier * digitCell.digit());
        multiplier *= 10;
      }
      return total;
    }

    private boolean isDigit(Point p) {
      return !isEmpty(p) && grid.get(p) instanceof Cell.Digit;
    }

    private boolean isFirstDigit(Point p) {
      var left = Point.of(p.x() - 1, p.y());
      return isEmpty(left) || grid.get(left) instanceof Cell.Symbol;
    }

    private boolean isEmpty(Point p) {
      return !grid.containsKey(p);
    }

    private List<Point> getDigitPointsFrom(Point p) {
      var points = new ArrayList<Point>();
      var i = 0;
      while (true) {
        var next = Point.of(p.x() + i, p.y());
        if (!isDigit(next)) {
          break;
        }
        points.add(next);
        i++;
      }
      return points;
    }

    private boolean isPartNumber(List<Point> points) {
      return points.stream().anyMatch(this::isAdjacentToSymbol);
    }

    private boolean isAdjacentToSymbol(Point p) {
      return p.neighbours().stream().anyMatch(this::isSymbol);
    }

    private boolean isSymbol(Point p) {
      return !isEmpty(p) && grid.get(p) instanceof Cell.Symbol;
    }

    public int sumGearRatios() {
      var partNumbers = findAllPartNumbers();
      return findPossibleGearPoints().stream()
          .map(p -> findAdjacentPartNumbers(p, partNumbers))
          .filter(Objects::nonNull)
          .filter(pns -> pns.size() == 2)
          .map(this::getGearRatio)
          .reduce(Integer::sum)
          .orElseThrow();
    }

    private List<PartNumber> findAdjacentPartNumbers(Point p, List<PartNumber> partNumbers) {
      var neighbours = new HashSet<>(p.neighbours());
      return partNumbers.stream()
          .filter(pn -> !Collections.disjoint(new HashSet<>(pn.points()), neighbours))
          .toList();
    }

    private int getGearRatio(List<PartNumber> partNumbers) {
      return partNumbers.stream().reduce(1, (acc, pn) -> acc * pn.value(), (a, b) -> a * b);
    }

    private List<Point> findPossibleGearPoints() {
      return IntStream.range(0, height)
          .mapToObj(y -> IntStream.range(0, width).mapToObj(x -> Point.of(x, y)))
          .reduce(Stream::concat)
          .orElseThrow()
          .filter(this::isPossibleGearPoint)
          .toList();
    }

    private boolean isPossibleGearPoint(Point p) {
      return isSymbol(p) && ((Cell.Symbol) grid.get(p)).symbol() == '*';
    }
  }
}
