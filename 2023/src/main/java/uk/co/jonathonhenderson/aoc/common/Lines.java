package uk.co.jonathonhenderson.aoc.common;

import java.util.stream.Stream;

public final class Lines {
  private Lines() {}

  public static Stream<String> splitByEmptyLines(String input) {
    return Stream.of(input.trim().split("(\r\n|\n|\r)\\s*(\r\n|\n|\r)"));
  }
}
