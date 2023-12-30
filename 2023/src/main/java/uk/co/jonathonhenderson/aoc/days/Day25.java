package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Pair;
import uk.co.jonathonhenderson.aoc.common.PathFinding;

public class Day25 extends Day {
  private final Wires wires;

  public Day25(String input) {
    this.wires = Wires.parse(input);
  }

  @Override
  public Optional<String> part1() {
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
      var cuts = findCutsUsingPathfindingHeuristic();
      var secondGroup = findSecondGroup(cuts.get(0), cuts.get(1), cuts.get(2)).orElseThrow();
      return (long) (nodes.size() - secondGroup.size()) * secondGroup.size();
    }

    private List<Pair<String, String>> findCutsUsingPathfindingHeuristic() {
      var commonlyTraversedEdges = new HashMap<Pair<String, String>, Integer>();
      for (var a : nodes) {
        for (var b : nodes) {
          if (a.equals(b)) {
            continue;
          }

          var path = findPathBetween(a, b);
          for (var wire : path) {
            var key = wire;
            var opposite = Pair.of(wire.second(), wire.first());
            if (!commonlyTraversedEdges.containsKey(wire)
                && commonlyTraversedEdges.containsKey(opposite)) {
              key = opposite;
            }

            var n = commonlyTraversedEdges.getOrDefault(key, 0);
            commonlyTraversedEdges.put(key, n + 1);
          }
        }
      }

      return commonlyTraversedEdges.entrySet().stream()
          .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
          .limit(10)
          .map(Entry::getKey)
          .toList();
    }

    private List<Pair<String, String>> findPathBetween(String a, String b) {
      var path = PathFinding.bfs(a, b, connections::get);
      if (path == null) {
        throw new IllegalStateException(
            "Did not expect to be unable to trace a path between " + a + "and " + b);
      }

      var wires = new ArrayList<Pair<String, String>>();
      for (var i = 0; i < path.size() - 1; i++) {
        wires.add(Pair.of(path.get(i), path.get(i + 1)));
      }
      return wires;
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
