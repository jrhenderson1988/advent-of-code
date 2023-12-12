package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    return answer();
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
  }

  private record ConditionRecord(List<Status> arrangement, List<Integer> groups) {
    public static ConditionRecord parse(String line) {
      var parts = line.trim().split("\\s+");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid condition record");
      }
      var arrangement = parts[0].trim().chars().mapToObj(c -> Status.of((char) c)).toList();
      var groups = Arrays.stream(parts[1].trim().split(",")).map(Integer::parseInt).toList();
      return new ConditionRecord(arrangement, groups);
    }

    public int totalPossibleArrangements() {
      var totalUnknowns = (int) arrangement.stream().filter(Status.UNKNOWN::equals).count();
      var total = 0;
      for (var i = 0; i <= getMaxIterations(totalUnknowns); i++) {
        var replacements = getReplacements(i, totalUnknowns);
        var possibleArrangement = applyReplacements(replacements);
        if (isArrangementPossible(possibleArrangement)) {
          total++;
        }
      }

      return total;
    }

    private boolean isArrangementPossible(List<Status> possibleArrangement) {
      return groups().equals(toGroup(possibleArrangement));
    }

    private List<Integer> toGroup(List<Status> possibleArrangement) {
      var group = new ArrayList<Integer>();

      var contiguous = 0;
      for (var s : possibleArrangement) {
        if (s.equals(Status.OPERATIONAL) && contiguous > 0) {
          group.add(contiguous);
          contiguous = 0;
        } else if (s.equals(Status.DAMAGED)) {
          contiguous++;
        }
      }

      if (contiguous > 0) {
        group.add(contiguous);
      }

      return group;
    }

    private List<Status> applyReplacements(List<Status> replacements) {
      var replaced = new ArrayList<Status>();
      var nextReplacement = 0;
      for (var status : arrangement) {
        if (status.equals(Status.UNKNOWN)) {
          replaced.add(replacements.get(nextReplacement));
          nextReplacement++;
        } else {
          replaced.add(status);
        }
      }

      return replaced;
    }

    private List<Status> getReplacements(int n, int size) {
      return toBinaryString(n, size).chars().mapToObj(ch -> toReplacement((char) ch)).toList();
    }

    private Status toReplacement(char c) {
      return switch (c) {
        case '1' -> Status.DAMAGED;
        case '0' -> Status.OPERATIONAL;
        default -> throw new IllegalArgumentException("Unexpected char");
      };
    }

    private String toBinaryString(int n, int chars) {
      return String.format("%" + chars + "s", Integer.toBinaryString(n)).replace(' ', '0');
    }

    private int getMaxIterations(int n) {
      return (0xffffffff << n) ^ 0xffffffff;
    }
  }

  private record ConditionRecords(List<ConditionRecord> records) {
    public static ConditionRecords parse(String input) {
      return new ConditionRecords(input.trim().lines().map(ConditionRecord::parse).toList());
    }

    public int sumOfPossibleArrangements() {
      return records.stream()
          .map(ConditionRecord::totalPossibleArrangements)
          .reduce(Integer::sum)
          .orElseThrow();
    }
  }
}
