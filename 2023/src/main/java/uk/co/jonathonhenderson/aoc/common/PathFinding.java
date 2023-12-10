package uk.co.jonathonhenderson.aoc.common;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        return tracePath(prev, v);
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

  private static <N> List<N> tracePath(Map<N, N> prev, N target) {
    var path = new ArrayList<N>();

    var curr = target;
    while (curr != null) {
      path.add(curr);
      curr = prev.get(curr);
    }
    return IntStream.range(0, path.size()).mapToObj(i -> path.get(path.size() - 1 - i)).toList();
  }
}
