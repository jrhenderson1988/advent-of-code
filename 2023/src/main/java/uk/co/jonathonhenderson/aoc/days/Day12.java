package uk.co.jonathonhenderson.aoc.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Triple;

public class Day12 extends Day {

  private final ConditionRecords conditionRecords;

  public Day12(String input) {
    this.conditionRecords = ConditionRecords.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(conditionRecords.sumOfPossibleArrangements());
  }

  @Override
  public Optional<String> part2() {
    // This was tough - took inspiration from this in the end:
    // https://github.com/jonathanpaulson/AdventOfCode/blob/master/2023/12.py
    return answer(conditionRecords.multiply(5).sumOfPossibleArrangements());
  }

  private enum Status {
    OPERATIONAL,
    DAMAGED,
    UNKNOWN;

    public static Status of(char ch) {
      return switch (ch) {
        case '.' -> OPERATIONAL;
        case '#' -> DAMAGED;
        case '?' -> UNKNOWN;
        default -> throw new IllegalArgumentException("Unknown status");
      };
    }

    @Override
    public String toString() {
      return switch (this) {
        case OPERATIONAL -> ".";
        case DAMAGED -> "#";
        case UNKNOWN -> "?";
      };
    }
  }

  public record ConditionRecord(List<Status> arrangement, List<Integer> groups) {
    public static ConditionRecord parse(String line) {
      var parts = line.trim().split("\\s+");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid condition record");
      }
      var arrangement = parts[0].trim().chars().mapToObj(c -> Status.of((char) c)).toList();
      var groups = Arrays.stream(parts[1].trim().split(",")).map(Integer::parseInt).toList();
      return new ConditionRecord(arrangement, groups);
    }

    public long totalPossibleArrangements() {
      return countPossibleArrangements(0, 0, 0, new HashMap<>());
    }

    private long countPossibleArrangements(
        int i, int gi, int currGroupSize, Map<Triple<Integer, Integer, Integer>, Long> cache) {
      var key = Triple.of(i, gi, currGroupSize);
      if (cache.containsKey(key)) {
        return cache.get(key);
      }

      if (i == arrangement.size()) {
        if (gi == groups.size() && currGroupSize == 0) {
          return 1;
        } else if (gi == groups.size() - 1 && groups.get(gi) == currGroupSize) {
          return 1;
        } else {
          return 0;
        }
      }

      var answer = 0L;
      if (getStatusAt(i).equals(Status.OPERATIONAL) || getStatusAt(i).equals(Status.UNKNOWN)) {
        if (currGroupSize == 0) {
          answer += countPossibleArrangements(i + 1, gi, 0, cache);
        } else if (currGroupSize > 0 && gi < groups.size() && groups.get(gi) == currGroupSize) {
          answer += countPossibleArrangements(i + 1, gi + 1, 0, cache);
        }
      }

      if (getStatusAt(i).equals(Status.DAMAGED) || getStatusAt(i).equals(Status.UNKNOWN)) {
        answer += countPossibleArrangements(i + 1, gi, currGroupSize + 1, cache);
      }

      cache.put(key, answer);

      return answer;
    }

    private Status getStatusAt(int n) {
      return arrangement.get(n);
    }

    @Override
    public String toString() {
      return arrangement.stream().map(Status::toString).collect(Collectors.joining())
          + " "
          + groups.stream().map(c -> "" + c).collect(Collectors.joining(","));
    }

    public ConditionRecord multiply(int times) {
      var newArrangement =
          IntStream.range(0, times)
              .mapToObj(
                  i ->
                      i == times - 1
                          ? arrangement.stream()
                          : Stream.concat(arrangement.stream(), Stream.of(Status.UNKNOWN)))
              .reduce(Stream::concat)
              .orElseThrow()
              .toList();
      var newGroups =
          IntStream.range(0, times)
              .mapToObj(i -> groups.stream())
              .reduce(Stream::concat)
              .orElseThrow()
              .toList();

      return new ConditionRecord(newArrangement, newGroups);
    }
  }

  private record ConditionRecords(List<ConditionRecord> records) {
    public static ConditionRecords parse(String input) {
      return new ConditionRecords(input.trim().lines().map(ConditionRecord::parse).toList());
    }

    public ConditionRecords multiply(int times) {
      return new ConditionRecords(records.stream().map(r -> r.multiply(times)).toList());
    }

    public long sumOfPossibleArrangements() {
      return records.stream()
          .map(ConditionRecord::totalPossibleArrangements)
          .reduce(Long::sum)
          .orElseThrow();
    }
  }
}
