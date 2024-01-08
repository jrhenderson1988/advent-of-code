package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day24 extends Day {

  private final Hailstones hailstones;

  public Day24(String input) {
    this.hailstones = Hailstones.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return part1(200000000000000d, 400000000000000d);
  }

  public Optional<String> part1(Double min, Double max) {
    return answer(hailstones.totalIntersectionsInTargetArea(min, max));
  }

  @Override
  public Optional<String> part2() {
    // Based on solution described and demonstrated in:
    // https://www.reddit.com/r/adventofcode/comments/18q40he/2023_day_24_part_2_a_straightforward_nonsolver/
    return answer(hailstones.sumOfCoordinatesOfRockPosition());
  }

  private record Coordinate(Double x, Double y, Double z) {
    public static Coordinate parse(String line) {
      var parts =
          Arrays.stream(line.trim().split(",")).map(String::trim).map(Double::valueOf).toList();
      if (parts.size() != 3) {
        throw new IllegalArgumentException("Invalid coordinate");
      }
      return new Coordinate(parts.get(0), parts.get(1), parts.get(2));
    }

    public Coordinate translate(Coordinate delta) {
      return new Coordinate(x + delta.x(), y + delta.y(), z + delta.z());
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
      var m = (second.y() - first.y()) / (second.x() - first.x());
      var b = position.y() - (m * position.x());

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

    public long totalIntersectionsInTargetArea(Double min, Double max) {
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
        Hailstone a, Hailstone b, Double min, Double max) {
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

    private boolean intersectionOccursInFuture(Hailstone hs, Double x, Double y) {
      if (hs.velocity().x() >= 0 && x < hs.position().x()) {
        return false;
      } else if (hs.velocity().x() < 0 && x > hs.position().x()) {
        return false;
      }

      if (hs.velocity().y() >= 0 && y < hs.position().y()) {
        return false;
      }

      return hs.velocity().y() >= 0 || y <= hs.position().y();
    }

    private boolean intersectionOccursOutsideBoundary(Double x, Double y, Double min, Double max) {
      return !intersectionOccursWithinBoundary(x, y, min, max);
    }

    private boolean intersectionOccursWithinBoundary(Double x, Double y, Double min, Double max) {
      return x >= min && x <= max && y >= min && y <= max;
    }

    public Long sumOfCoordinatesOfRockPosition() {
      var first =
          elim(
                  mat(
                      hs -> hs.position().x(),
                      hs -> hs.position().y(),
                      hs -> hs.velocity().x(),
                      hs -> hs.velocity().y()))
              .stream()
              .map(List::getLast)
              .toList();
      var x = first.get(0);
      var y = first.get(1);

      var second =
          elim(
                  mat(
                      hs -> hs.position().z(),
                      hs -> hs.position().y(),
                      hs -> hs.velocity().z(),
                      hs -> hs.velocity().y()))
              .stream()
              .map(List::getLast)
              .toList();
      var z = second.getFirst();

      return (long) (x + y + z);
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

  private record LineEquation(Double m, Double b) {
    public boolean isParallelTo(LineEquation other) {
      return this.m().equals(other.m());
    }

    public Double xIntersection(LineEquation other) {
      return (other.b() - this.b()) / (this.m() - other.m());
    }

    public Double yIntersection(LineEquation other) {
      return (this.m() * xIntersection(other)) + this.b();
    }
  }
}
