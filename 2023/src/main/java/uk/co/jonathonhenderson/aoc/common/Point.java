package uk.co.jonathonhenderson.aoc.common;

import java.util.List;

public record Point(int x, int y) {
  public static Point of(int x, int y) {
    return new Point(x, y);
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
}
