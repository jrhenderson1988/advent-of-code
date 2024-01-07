package uk.co.jonathonhenderson.aoc.days;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day24 extends Day {
  private static final int SCALE = 200;
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

  private final Hailstones hailstones;

  public Day24(String input) {
    this.hailstones = Hailstones.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return part1(new BigDecimal("200000000000000"), new BigDecimal("400000000000000"));
  }

  public Optional<String> part1(BigDecimal min, BigDecimal max) {
    return answer(hailstones.totalIntersectionsInTargetArea(min, max));
  }

  @Override
  public Optional<String> part2() {
    // Based on solution described and demonstrated in:
    // https://www.reddit.com/r/adventofcode/comments/18q40he/2023_day_24_part_2_a_straightforward_nonsolver/
    return answer(hailstones.sumOfCoordinatesOfRockPosition());
  }

  private record Coordinate(BigDecimal x, BigDecimal y, BigDecimal z) {
    public static Coordinate parse(String line) {
      var parts =
          Arrays.stream(line.trim().split(",")).map(String::trim).map(BigDecimal::new).toList();
      if (parts.size() != 3) {
        throw new IllegalArgumentException("Invalid coordinate");
      }
      return new Coordinate(parts.get(0), parts.get(1), parts.get(2));
    }

    public Coordinate translate(Coordinate delta) {
      return new Coordinate(x.add(delta.x()), y.add(delta.y()), z.add(delta.z()));
    }

    @Override
    public String toString() {
      return x + ", " + y + ", " + z;
    }
  }

  private record Hailstone(Coordinate position, Coordinate velocity) {
    public static Hailstone parse(String line) {
      var parts = line.trim().split("@");
      var position = Coordinate.parse(parts[0]);
      var velocity = Coordinate.parse(parts[1]);
      return new Hailstone(position, velocity);
    }

    public LineEquation toEquation() {
      var first = position;
      var second = position.translate(velocity);
      var m =
          second
              .y()
              .subtract(first.y())
              .divide(second.x().subtract(first.x()), SCALE, ROUNDING_MODE);
      var b = position.y().subtract(m.multiply(position.x()));

      return new LineEquation(m, b);
    }

    @Override
    public String toString() {
      return position + " @ " + velocity;
    }
  }

  private record Hailstones(List<Hailstone> hailstones) {
    public static Hailstones parse(String input) {
      return new Hailstones(input.trim().lines().map(Hailstone::parse).toList());
    }

    public long totalIntersectionsInTargetArea(BigDecimal min, BigDecimal max) {
      var total = 0;
      for (var i = 0; i < hailstones.size(); i++) {
        var a = hailstones.get(i);
        for (var j = i + 1; j < hailstones.size(); j++) {
          var b = hailstones.get(j);
          if (hailstonePathsIntersectInTargetArea(a, b, min, max)) {
            total++;
          }
        }
      }
      return total;
    }

    private boolean hailstonePathsIntersectInTargetArea(
        Hailstone a, Hailstone b, BigDecimal min, BigDecimal max) {
      var lineA = a.toEquation();
      var lineB = b.toEquation();

      if (lineA.isParallelTo(lineB)) {
        return false;
      }

      var intersectionX = lineA.xIntersection(lineB);
      var intersectionY = lineA.yIntersection(lineB);
      if (intersectionOccursOutsideBoundary(intersectionX, intersectionY, min, max)) {
        return false;
      }

      return intersectionOccursInFuture(a, intersectionX, intersectionY)
          && intersectionOccursInFuture(b, intersectionX, intersectionY);
    }

    private boolean intersectionOccursInFuture(Hailstone hs, BigDecimal x, BigDecimal y) {
      if (isPositive(hs.velocity().x()) && lt(x, hs.position().x())) {
        return false;
      } else if (isNegative(hs.velocity().x()) && gt(x, hs.position().x())) {
        return false;
      }

      if (isPositive(hs.velocity().y()) && lt(y, hs.position().y())) {
        return false;
      } else if (isNegative(hs.velocity().y()) && gt(y, hs.position().y())) {
        return false;
      }

      return true;
    }

    private boolean isNegative(BigDecimal num) {
      return !isPositive(num);
    }

    private boolean isPositive(BigDecimal num) {
      return num.compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean gt(BigDecimal a, BigDecimal b) {
      return a.compareTo(b) > 0;
    }

    private boolean gte(BigDecimal a, BigDecimal b) {
      return a.compareTo(b) >= 0;
    }

    private boolean lt(BigDecimal a, BigDecimal b) {
      return a.compareTo(b) < 0;
    }

    private boolean lte(BigDecimal a, BigDecimal b) {
      return a.compareTo(b) <= 0;
    }

    private boolean intersectionOccursOutsideBoundary(
        BigDecimal x, BigDecimal y, BigDecimal min, BigDecimal max) {
      return !intersectionOccursWithinBoundary(x, y, min, max);
    }

    private boolean intersectionOccursWithinBoundary(
        BigDecimal x, BigDecimal y, BigDecimal min, BigDecimal max) {
      return gte(x, min) && lte(x, max) && gte(y, min) && lte(y, max);
    }

    public Long sumOfCoordinatesOfRockPosition() {
      var first =
          elim(
                  mat(
                      hs -> hs.position().x().doubleValue(),
                      hs -> hs.position().y().doubleValue(),
                      hs -> hs.velocity().x().doubleValue(),
                      hs -> hs.velocity().y().doubleValue()))
              .stream()
              .map(List::getLast)
              .toList();
      var x = first.get(0);
      var y = first.get(1);

      var second =
          elim(
                  mat(
                      hs -> hs.position().z().doubleValue(),
                      hs -> hs.position().y().doubleValue(),
                      hs -> hs.velocity().z().doubleValue(),
                      hs -> hs.velocity().y().doubleValue()))
              .stream()
              .map(List::getLast)
              .toList();
      var z = second.getFirst();

      var result = x + y + z;

      return (long) result;
    }

    private List<List<Double>> mat(
        Function<Hailstone, Double> x,
        Function<Hailstone, Double> y,
        Function<Hailstone, Double> dx,
        Function<Hailstone, Double> dy) {
      var m =
          hailstones.stream()
              .map(
                  s ->
                      List.of(
                          -dy.apply(s),
                          dx.apply(s),
                          y.apply(s),
                          -x.apply(s),
                          y.apply(s) * dx.apply(s) - x.apply(s) * dy.apply(s)))
              .toList();
      var last = m.getLast();
      return m.stream()
          .limit(4)
          .map(
              r ->
                  IntStream.range(0, r.size()).mapToObj(idx -> r.get(idx) - last.get(idx)).toList())
          .toList();
    }

    private List<List<Double>> elim(List<List<Double>> m) {
      m = new ArrayList<>(m.stream().toList());

      for (var i = 0; i < m.size(); i++) {
        var t = m.get(i).get(i);
        var newMi = new ArrayList<Double>();
        for (var x : m.get(i)) {
          newMi.add(x / t);
        }
        m.set(i, newMi);
        for (var j = i + 1; j < m.size(); j++) {
          t = m.get(j).get(i);
          var mj = m.get(j);
          var newMj = new ArrayList<Double>();
          for (var k = 0; k < mj.size(); k++) {
            var x = mj.get(k);
            newMj.add(x - (t * m.get(i).get(k)));
          }
          m.set(j, newMj);
        }
      }

      for (var i = m.size() - 1; i >= 0; i--) {
        for (var j = 0; j < i; j++) {
          var t = m.get(j).get(i);
          var mj = m.get(j);
          var newMj = new ArrayList<Double>();
          for (var k = 0; k < mj.size(); k++) {
            var x = mj.get(k);
            newMj.add(x - (t * m.get(i).get(k)));
          }
          m.set(j, newMj);
        }
      }

      return m;
    }
  }

  private record LineEquation(BigDecimal m, BigDecimal b) {
    public static LineEquation of(BigDecimal m, BigDecimal b) {
      return new LineEquation(m, b);
    }

    public boolean isParallelTo(LineEquation other) {
      return this.m().equals(other.m());
    }

    public BigDecimal xIntersection(LineEquation other) {
      return other
          .b()
          .subtract(this.b())
          .divide(this.m().subtract(other.m()), SCALE, ROUNDING_MODE);
    }

    public BigDecimal yIntersection(LineEquation other) {
      return this.m().multiply(xIntersection(other)).add(this.b());
    }
  }
}
