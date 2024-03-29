package uk.co.jonathonhenderson.aoc.common;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PathFinding {
  private PathFinding() {}

  public static <N> List<N> bfs(N root, N target, Function<N, Iterable<N>> neighboursFn) {
    var q = new ArrayDeque<N>();
    var explored = new HashSet<N>();
    var prev = new HashMap<N, N>();

    q.add(root);
    explored.add(root);
    prev.put(root, null);

    while (!q.isEmpty()) {
      var v = q.poll();
      if (v.equals(target)) {
        return bfsTracePath(prev, v);
      }

      for (var w : neighboursFn.apply(v)) {
        if (!explored.contains(w)) {
          explored.add(w);
          prev.put(w, v);
          q.add(w);
        }
      }
    }

    return null;
  }

  private static <N> List<N> bfsTracePath(Map<N, N> prev, N target) {
    var path = new ArrayList<N>();

    var curr = target;
    while (curr != null) {
      path.add(curr);
      curr = prev.get(curr);
    }
    return IntStream.range(0, path.size()).mapToObj(i -> path.get(path.size() - 1 - i)).toList();
  }

  public static <T> List<T> dijkstra(
      Collection<T> points,
      T source,
      T target,
      BiFunction<T, Map<T, T>, List<Pair<T, Integer>>> neighbours) {
    var q = new HashSet<>(points);
    var dist = q.stream().collect(Collectors.toMap(p -> p, ignored -> Integer.MAX_VALUE));
    var prev = new HashMap<T, T>();
    for (var p : q) {
      prev.put(p, null);
    }

    dist.put(source, 0);
    while (!q.isEmpty()) {
      var u =
          dist.entrySet().stream()
              .filter(e -> q.contains(e.getKey()))
              .min(Comparator.comparingInt(Entry::getValue))
              .orElseThrow()
              .getKey();
      q.remove(u);

      if (u.equals(target)) {
        return tracePathDijkstra(source, target, prev);
      }

      for (var n : neighbours.apply(u, prev).stream().filter(o -> q.contains(o.left())).toList()) {
        var v = n.left();
        var cost = n.right();
        var alt = dist.getOrDefault(u, 0) + cost;
        if (alt < dist.get(v)) {
          dist.put(v, alt);
          prev.put(v, u);
        }
      }
    }

    return null;
  }

  // Note: This is modified from the Wikipedia version with a min priority queue. The alternative
  // approach of not filling the PQ with all the nodes in initialisation, and instead initialising
  // it only with source was taken. This means that the "decrease priority" part inside the
  // `if alt < dist[v]` block becomes an add with priority when the node is not already in the
  // queue. We're also no longer filling dist with MAX_INT values for all nodes or filling prev
  // initially with null values, since they are not needed. Instead, any calls to get the distance
  // for a node have been changed to `dist.getOrDefault(node, MAX_INT)`. These changes mean that we
  // no longer have to specify the entire set of nodes when calling the function and the
  // intermediate nodes/neighbours can be generated by the neighbours Function.
  //
  public static <T> List<T> dijkstraWithPriorityQueue(
      T source, Predicate<T> isTarget, Function<T, List<Pair<T, Integer>>> neighbours) {
    var prev = new HashMap<T, T>();
    var dist = new HashMap<T, Integer>();
    var q = new PriorityQueue<Node<T>>();

    dist.put(source, 0);
    q.add(Node.create(source, 0));

    while (!q.isEmpty()) {
      var first = q.poll();

      var u = first.value();
      for (var n : neighbours.apply(u)) {
        var v = n.left();
        var cost = n.right();
        var alt = dist.getOrDefault(u, Integer.MAX_VALUE) + cost;
        if (alt < dist.getOrDefault(v, Integer.MAX_VALUE)) {
          dist.put(v, alt);
          prev.put(v, u);

          var nn = Node.create(v, alt);
          if (!q.contains(nn)) {
            q.add(nn);
          }
        }
      }
    }

    var target = prev.keySet().stream().filter(isTarget).findFirst().orElseThrow();
    return tracePathDijkstra(source, target, prev);
  }

  private static <T> List<T> tracePathDijkstra(T source, T target, Map<T, T> prev) {
    var s = new ArrayList<T>();
    var n = target;
    if (prev.get(n) != null || n.equals(source)) {
      while (n != null) {
        s.add(n);
        n = prev.get(n);
      }
    }

    return s.reversed();
  }

  private record Node<T>(T value, int priority) implements Comparable<Node<T>> {

    public static <T> Node<T> create(T value, int priority) {
      return new Node<>(value, priority);
    }

    @Override
    public int compareTo(Node<T> o) {
      return Integer.compare(this.priority(), o.priority());
    }
  }
}
