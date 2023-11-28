package uk.co.jonathonhenderson.aoc.launcher;

public sealed interface Parts {
  static Parts.Both both() {
    return new Parts.Both();
  }

  static Parts.First first() {
    return new Parts.First();
  }

  static Parts.Second second() {
    return new Parts.Second();
  }

  record Both() implements Parts {}
  record First() implements Parts {}
  record Second() implements Parts {}
}
