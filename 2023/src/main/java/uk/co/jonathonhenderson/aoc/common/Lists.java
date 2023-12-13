package uk.co.jonathonhenderson.aoc.common;

import java.util.List;
import java.util.stream.IntStream;

public final class Lists {
  private Lists() {}

  public static <T> List<T> reverse(List<T> input) {
    return IntStream.range(0, input.size()).mapToObj(i -> input.get(input.size() - 1 - i)).toList();
  }
}
