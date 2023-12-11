package uk.co.jonathonhenderson.aoc.common;

import java.util.List;

public record Point(long x, long y) {
  public static Point of(long x, long y) {
    return new Point(x, y);
  }

  public static Point of(int x, int y) {
    return Point.of((long) x, (long) y);
  }

  public Point translate(Point other) {
    return Point.of(this.x + other.x(), this.y + other.y());
  }

  public List<Point> neighbours() {
    return List.of(
        Point.of(x - 1, y - 1),
        Point.of(x, y - 1),
        Point.of(x + 1, y - 1),
        Point.of(x - 1, y),
        Point.of(x + 1, y),
        Point.of(x - 1, y + 1),
        Point.of(x, y + 1),
        Point.of(x + 1, y + 1));
  }

  public Point flip() {
    return Point.of(this.x * -1, this.y * -1);
  }

  public long manhattanDistanceTo(Point other) {
    return Math.abs(this.x() - other.x()) + Math.abs(this.y() - other.y());
  }
}
