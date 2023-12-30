package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Pair;

public class Day25 extends Day {
  private final Wires wires;

  public Day25(String input) {
    this.wires = Wires.parse(input);
  }

  @Override
  public Optional<String> part1() {
    //    var result = wires.splitsIntoTwoGroups(Pair.of("hfx", "pzl"), Pair.of("bvb", "cmg"),
    // Pair.of("nvd", "jqt"));
    //    System.out.println(result);
    return answer(wires.findProductOfSizeOfHalvesWhenSplitIntoTwo());
  }

  @Override
  public Optional<String> part2() {
    return Optional.of("Day 25 has no part 2");
  }

  private static class Wires {
    private final Map<String, Set<String>> connections;
    private final Set<String> nodes;

    public Wires(Map<String, Set<String>> connections) {
      this.connections = connections;
      this.nodes =
          connections.entrySet().stream()
              .map(e -> Stream.concat(Stream.of(e.getKey()), e.getValue().stream()))
              .reduce(Stream::concat)
              .orElseThrow()
              .collect(Collectors.toSet());
    }

    public static Wires parse(String input) {
      var connections = new HashMap<String, Set<String>>();

      for (var line : input.trim().lines().toList()) {
        var parts = line.trim().split(":");
        var a = parts[0].trim();
        for (var b : parts[1].split(" ")) {
          if (b.isBlank()) {
            continue;
          }

          b = b.trim();

          var aConnections = connections.getOrDefault(a, new HashSet<>());
          aConnections.add(b);
          connections.put(a, aConnections);

          var bConnections = connections.getOrDefault(b, new HashSet<>());
          bConnections.add(a);
          connections.put(b, bConnections);
          connections.put(b, bConnections);
        }
      }

      return new Wires(connections);
    }

    public long findProductOfSizeOfHalvesWhenSplitIntoTwo() {
      var possibleCuts = findPossibleCuts();
      System.out.println("Possible cuts: " + possibleCuts.size());

      for (var a : possibleCuts) {
        System.out.println("first cut: " + a);
        for (var b : possibleCuts) {
          for (var c : possibleCuts) {
            if (a.equals(b) || a.equals(c) || b.equals(c)) {
              continue;
            }

            var secondGroup = findSecondGroup(a, b, c);
            if (secondGroup.isPresent()) {
              return (long) (nodes.size() - secondGroup.get().size()) * secondGroup.get().size();
            }
          }
        }
      }

      throw new IllegalStateException("Could not find cuts");
    }

    private Set<Pair<String, String>> findPossibleCuts() {
      var possibleCuts = new HashSet<Pair<String, String>>();
      for (var connection : connections.entrySet()) {
        var from = connection.getKey();
        for (var to : connection.getValue()) {
          if (!possibleCuts.contains(Pair.of(from, to))
              && !possibleCuts.contains(Pair.of(to, from))) {
            possibleCuts.add(Pair.of(from, to));
          }
        }
      }
      return possibleCuts;
    }

    private Optional<Set<String>> findSecondGroup(
        Pair<String, String> a, Pair<String, String> b, Pair<String, String> c) {
      var initial = nodes.stream().findFirst().orElseThrow();

      var queue = new ArrayDeque<String>();
      queue.add(initial);

      var seen = new HashSet<String>();
      var otherGroup = new HashSet<String>();
      while (!queue.isEmpty()) {
        var from = queue.poll();
        if (seen.contains(from)) {
          continue;
        }
        seen.add(from);

        for (var to : connections.get(from)) {
          if (matchesCut(from, to, a) || matchesCut(from, to, b) || matchesCut(from, to, c)) {
            continue;
          }

          otherGroup.add(from);
          queue.add(to);
        }

        if (otherGroup.size() == nodes.size()) {
          return Optional.empty();
        }
      }

      return Optional.of(otherGroup);
    }

    private boolean matchesCut(String from, String to, Pair<String, String> cut) {
      if (from.equals(cut.first()) && to.equals(cut.second())) {
        return true;
      } else if (from.equals(cut.second()) && to.equals(cut.first())) {
        return true;
      }
      return false;
    }

    @Override
    public String toString() {
      return "Nodes:\n"
          + nodes.toString()
          + "\n\nWires: \n"
          + connections.entrySet().stream()
              .map(e -> e.getKey() + ": " + e.getValue())
              .collect(Collectors.joining("\n"));
    }
  }
}
