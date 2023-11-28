package uk.co.jonathonhenderson.aoc.launcher;

public sealed interface Days {
  static Days.All all() {
    return new Days.All();
  }

  static Days.Single single(int day) {
    return new Days.Single(day);
  }

  record All() implements Days {}

  record Single(int day) implements Days {}
}
