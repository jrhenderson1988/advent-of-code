package uk.co.jonathonhenderson.aoc.days;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day06 extends Day {

  private final RaceDocument races;

  public Day06(String input) {
    this.races = RaceDocument.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(races.productOfWaysToWin());
  }

  @Override
  public Optional<String> part2() {
    return answer(races.getCorrectedRace().totalWaysToWin());
  }

  private record RaceDocument(List<Race> races) {
    public static RaceDocument parse(String input) {
      var lines = input.trim().lines().toList();
      if (lines.size() != 2) {
        throw new IllegalArgumentException("Unable to parse race document");
      }

      var timeLimits =
          Stream.of(lines.get(0).split("Time:")[1].trim().split("\\s+"))
              .map(Long::parseLong)
              .toList();
      var distances =
          Stream.of(lines.get(1).split("Distance:")[1].trim().split("\\s+"))
              .map(Long::parseLong)
              .toList();
      var races =
          LongStream.range(0, timeLimits.size())
              .mapToObj(i -> new Race(timeLimits.get((int) i), distances.get((int) i)))
              .toList();
      return new RaceDocument(races);
    }

    public long productOfWaysToWin() {
      return races.stream().map(Race::totalWaysToWin).reduce((a, b) -> a * b).orElseThrow();
    }

    public Race getCorrectedRace() {
      var timeLimit =
          Long.parseLong(
              races.stream()
                  .map(r -> String.valueOf(r.timeLimit()))
                  .reduce((a, b) -> a + b)
                  .orElseThrow());
      var record =
          Long.parseLong(
              races.stream()
                  .map(r -> String.valueOf(r.record()))
                  .reduce((a, b) -> a + b)
                  .orElseThrow());
      return new Race(timeLimit, record);
    }
  }

  private record Race(long timeLimit, long record) {
    public int totalWaysToWin() {
      return LongStream.range(1, timeLimit)
          .mapToObj(this::distanceTravelledHoldingFor)
          .filter(dist -> dist > record)
          .toList()
          .size();
    }

    private long distanceTravelledHoldingFor(long holdTime) {
      return (timeLimit - holdTime) * holdTime;
    }
  }
}
