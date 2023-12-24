package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day22 extends Day {

  private final Bricks bricks;

  public Day22(String input) {
    this.bricks = Bricks.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(bricks.totalDisintegratableBricks());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private record Coordinate(int x, int y, int z) {
    public static Coordinate of(int x, int y, int z) {
      return new Coordinate(x, y, z);
    }

    public static Coordinate parse(String input) {
      var values = Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList();
      if (values.size() != 3) {
        throw new IllegalArgumentException("Invalid coordinate");
      }
      return Coordinate.of(values.get(0), values.get(1), values.get(2));
    }

    public Coordinate translateZ(int amount) {
      return Coordinate.of(x, y, z + amount);
    }

    @Override
    public String toString() {
      return x + "," + y + "," + z;
    }
  }

  private record Brick(Coordinate a, Coordinate b) {
    public static Brick of(Coordinate a, Coordinate b) {
      return new Brick(a, b);
    }

    public static Brick parse(String line) {
      var parts = line.trim().split("~");
      var a = Coordinate.parse(parts[0]);
      var b = Coordinate.parse(parts[1]);
      if (a.z() > b.z()) {
        throw new IllegalStateException("a.z() coord is higher then b.z() coord");
      }
      if (Stream.of(a.x() - b.x(), a.y() - b.y(), a.z() - b.z()).filter(d -> d != 0).count() > 1) {
        throw new IllegalArgumentException("brick differs in more than one coordinate");
      }

      return Brick.of(a, b);
    }

    public int xSize() {
      return a.x() > b.x() ? a.x() - b.x() : b.x() - a.x();
    }

    public int ySize() {
      return a.y() > b.y() ? a.y() - b.y() : b.y() - a.y();
    }

    public int zSize() {
      return a.z() > b.z() ? a.z() - b.z() : b.z() - a.z();
    }

    public int minZ() {
      return Math.min(a.z(), b.z());
    }

    public Brick translateZ(int amount) {
      return Brick.of(a.translateZ(amount), b.translateZ(amount));
    }

    public boolean overlapsWith(Brick other) {
      var cubes = getAllCubes();
      cubes.retainAll(other.getAllCubes());
      return !cubes.isEmpty();
    }

    private Set<Coordinate> getAllCubes() {
      var cubes = new HashSet<Coordinate>();

      var minX = Math.min(a.x(), b.x());
      var maxX = Math.max(a.x(), b.x());
      var minY = Math.min(a.y(), b.y());
      var maxY = Math.max(a.y(), b.y());
      var minZ = Math.min(a.z(), b.z());
      var maxZ = Math.max(a.z(), b.z());

      for (var x = minX; x <= maxX; x++) {
        for (var y = minY; y <= maxY; y++) {
          for (var z = minZ; z <= maxZ; z++) {
            cubes.add(Coordinate.of(x, y, z));
          }
        }
      }

      return cubes;
    }

    @Override
    public String toString() {
      return a + "~" + b;
    }
  }

  private record Bricks(List<Brick> bricks) {
    public static Bricks parse(String input) {
      return new Bricks(input.trim().lines().map(Brick::parse).toList());
    }

    public int totalDisintegratableBricks() {
      return settle().findDisintegratableBricks().size();
    }

    @Override
    public String toString() {
      return bricks.stream().map(Record::toString).collect(Collectors.joining("\n"));
    }

    private SettledBricks settle() {
      var sorted = bricks.stream().sorted(Comparator.comparingInt(brick -> brick.a().z())).toList();

      var supports = new ArrayList<List<Integer>>();
      var newBricks = new ArrayList<Brick>();
      for (var i = 0; i < sorted.size(); i++) {
        var newBrick = sorted.get(i);
        while (true) {
          if (newBrick.minZ() == 1) {
            break;
          }

          var moved = newBrick.translateZ(-1);

          var supportedBy =
              IntStream.range(0, newBricks.size())
                  .filter(n -> newBricks.get(n).overlapsWith(moved))
                  .boxed()
                  .toList();
          if (!supportedBy.isEmpty()) {
            for (var s : supportedBy) {
              supports.get(s).add(i);
            }
            break;
          }

          // assign next position
          newBrick = moved;
        }

        newBricks.add(newBrick);
        supports.add(new ArrayList<>());
      }

      return new SettledBricks(new Bricks(newBricks), supports);
    }
  }

  private record SettledBricks(Bricks bricks, List<List<Integer>> supports) {
    public List<Brick> findDisintegratableBricks() {
      var disintegratableBricks = new ArrayList<Brick>();
      for (var brickIdx = 0; brickIdx < supports.size(); brickIdx++) {
        var brick = bricks.bricks().get(brickIdx);
        var supported = supports.get(brickIdx);

        var canBeDisintegrated = true;
        for (var s : supported) {
          var supports = findAllSupportsFor(s);
          if (supports.size() == 1) {
            canBeDisintegrated = false;
            break;
          }
        }

        if (canBeDisintegrated) {
          disintegratableBricks.add(brick);
        }
      }
      return disintegratableBricks;
    }

    private List<Integer> findAllSupportsFor(int brickIndex) {
      return IntStream.range(0, brickIndex)
          .filter(i -> supports.get(i).contains(brickIndex))
          .boxed()
          .toList();
    }
  }
}
