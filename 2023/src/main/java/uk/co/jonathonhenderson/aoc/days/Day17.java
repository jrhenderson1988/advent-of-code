package uk.co.jonathonhenderson.aoc.days;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Predicate;
import uk.co.jonathonhenderson.aoc.common.Direction;
import uk.co.jonathonhenderson.aoc.common.Point;

public class Day17 extends Day {

  private final Grid grid;

  public Day17(String input) {
    this.grid = Grid.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(grid.findOptimumPathWithNormalCrucible());
  }

  @Override
  public Optional<String> part2() {
    return answer(grid.findOptimumPathWithUltraCrucible());
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

  private record NodeProposal(Node current, Node proposed) {
    public static NodeProposal of(Node current, Node proposed) {
      return new NodeProposal(current, proposed);
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

    private int findOptimumPathWithNormalCrucible() {
      var start = Point.of(0, 0);
      var target = Point.of(getWidth() - 1, getHeight() - 1);
      return findOptimumPath(
          start, this::isValidProposalForNormalCrucible, key -> keyHitsTarget(key, target));
    }

    public int findOptimumPathWithUltraCrucible() {
      var start = Point.of(0, 0);
      var target = Point.of(getWidth() - 1, getHeight() - 1);
      return findOptimumPath(
          start,
          this::isValidProposalForUltraCrucible,
          key -> keyHitsTarget(key, target)&& key.distanceInDirection() >= 4);
    }

    private boolean isValidProposalForNormalCrucible(NodeProposal proposal) {
      var curr = proposal.current();
      var proposed = proposal.proposed();
      var backwards = proposed.direction().opposite().equals(curr.direction());
      return !backwards && proposed.distanceInDirection() <= 3;
    }

    private boolean keyHitsTarget(Key key, Point target) {
      return key.point().equals(target);
    }

    private boolean isValidProposalForUltraCrucible(NodeProposal proposal) {
      var curr = proposal.current();
      var isFirstStep = curr.direction() == null;
      if (isFirstStep) {
        return true;
      }

      var proposed = proposal.proposed();

      var backwards = proposed.direction().opposite().equals(curr.direction());
      var tooManySteps = proposed.distanceInDirection() > 10;
      if (backwards || tooManySteps) {
        return false;
      }

      var movedMinimumSteps = curr.distanceInDirection() >= 4;
      var movingSameDirection = proposed.direction().equals(curr.direction());
      return movedMinimumSteps || movingSameDirection;
    }

    public int findOptimumPath(
        Point start, Predicate<NodeProposal> proposalValidator, Predicate<Key> isTarget) {
      var height = getHeight();
      var width = getWidth();

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
          if (inBounds(neighbour, width, height)) {
            var proposed =
                Node.of(
                    curr.distance() + valueAt(neighbour),
                    neighbour,
                    newDirection,
                    newDistanceInDirection);
            if (proposalValidator.test(NodeProposal.of(curr, proposed))) {
              q.add(proposed);
            }
          }
        }
      }

      return distances.entrySet().stream()
          .filter(e -> isTarget.test(e.getKey()))
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
