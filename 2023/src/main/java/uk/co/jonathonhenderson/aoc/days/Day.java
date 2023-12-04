package uk.co.jonathonhenderson.aoc.days;

import java.util.Optional;

public abstract class Day {
  public abstract Optional<String> part1();

  public abstract Optional<String> part2();

  protected Optional<String> answer(int answer) {
    return Optional.of(String.valueOf(answer));
  }

  protected Optional<String> nothing() {
    return Optional.empty();
  }
}
