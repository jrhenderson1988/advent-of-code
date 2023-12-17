package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.PriorityQueue;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day17 extends Day {

  private final Grid grid;

  public Day17(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(grid.findOptimumPath());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private record Node(int distance, Point point, Direction direction, int distanceInDirection)
      implements Comparable<Node> {
    public static Node of(int distance, Point point, Direction direction, int distanceInDirection) {
      return new Node(distance, point, direction, distanceInDirection);
    }

    @Override
    public int compareTo(Node o) {
      // priority defined by the distance of the node
      return Integer.compare(this.distance, o.distance);
    }
  }

  private record Key(Point point, Direction direction, int distanceInDirection) {
    public static Key fromNode(Node node) {
      return new Key(node.point(), node.direction(), node.distanceInDirection());
    }
  }

  private record Grid(List<List<Integer>> cells) {
    public static Grid parse(String input) {
      return new Grid(
          input
              .trim()
              .lines()
              .map(
                  line ->
                      line.trim()
                          .chars()
                          .mapToObj(ch -> Integer.parseInt(((char) ch) + ""))
                          .toList())
              .toList());
    }

    private int getHeight() {
      return cells.size();
    }

    private int getWidth() {
      return cells.getFirst().size();
    }

    public int findOptimumPath() {
      var height = getHeight();
      var width = getWidth();
      var start = Point.of(0, 0);
      var target = Point.of(width - 1, height - 1);

      var q = new PriorityQueue<Node>();
      q.add(Node.of(0, start, null, -1));

      var distances = new HashMap<Key, Integer>();
      while (!q.isEmpty()) {
        var curr = q.poll();
        var key = Key.fromNode(curr);
        if (distances.containsKey(key)) {
          continue;
        }

        distances.put(key, curr.distance());
        for (var newDirection : Direction.values()) {
          var neighbour = curr.point().translate(newDirection.delta());
          var newDistanceInDirection =
              newDirection.equals(curr.direction()) ? curr.distanceInDirection() + 1 : 1;
          if (inBounds(neighbour, width, height)
              && !newDirection.opposite().equals(curr.direction())
              && newDistanceInDirection <= 3) {
            var cost = valueAt(neighbour);
            q.add(Node.of(curr.distance() + cost, neighbour, newDirection, newDistanceInDirection));
          }
        }
      }

      return distances.entrySet().stream()
          .filter(e -> e.getKey().point().equals(target))
          .map(Entry::getValue)
          .reduce(Integer.MAX_VALUE, Math::min);
    }

    private int valueAt(Point pt) {
      return valueAt(pt.x(), pt.y());
    }

    private int valueAt(long x, long y) {
      return valueAt((int) x, (int) y);
    }

    private int valueAt(int x, int y) {
      return cells.get(y).get(x);
    }

    private boolean inBounds(Point pt, int width, int height) {
      return pt.x() >= 0 && pt.x() < width && pt.y() >= 0 && pt.y() < height;
    }
  }
}
