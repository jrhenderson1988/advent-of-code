package uk.co.jonathonhenderson.aoc.common;

public enum Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST;

  public Point delta() {
    return switch (this) {
      case NORTH -> Point.of(0, -1);
      case EAST -> Point.of(1, 0);
      case SOUTH -> Point.of(0, 1);
      case WEST -> Point.of(-1, 0);
    };
  }

  public Direction opposite() {
    return switch (this) {
      case NORTH -> SOUTH;
      case EAST -> WEST;
      case SOUTH -> NORTH;
      case WEST -> EAST;
    };
  }
}
