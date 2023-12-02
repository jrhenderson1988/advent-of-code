package uk.co.jonathonhenderson.aoc.days;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Day02 extends Day {

  private final List<Game> games;

  public Day02(String input) {
    this.games = parse(input);
  }

  private static List<Game> parse(String input) {
    return input.trim().lines().map(Game::parse).toList();
  }

  @Override
  public Optional<String> part1() {
    return games.stream()
        .filter(Game::isPossible)
        .map(Game::id)
        .reduce(Integer::sum)
        .map(String::valueOf);
  }

  @Override
  public Optional<String> part2() {
    return games.stream().map(Game::power).reduce(Integer::sum).map(String::valueOf);
  }

  record Game(int id, List<Handful> handfuls) {
    private static final int MAX_RED = 12;
    private static final int MAX_GREEN = 13;
    private static final int MAX_BLUE = 14;

    public static Game parse(String line) {
      var parts = line.split(": ");
      var id = Integer.parseInt(parts[0].split("Game")[1].trim());
      var handfuls = Stream.of(parts[1].split(";")).map(Handful::parse).toList();
      return new Game(id, handfuls);
    }

    public boolean isPossible() {
      var max = this.handfuls().stream().reduce(Handful::max).orElseThrow();
      return max.red() <= MAX_RED && max.green() <= MAX_GREEN && max.blue() <= MAX_BLUE;
    }

    public int power() {
      var max = this.handfuls().stream().reduce(Handful::max).orElseThrow();
      return max.red() * max.green() * max.blue();
    }
  }

  record Handful(int red, int green, int blue) {

    public static Handful parse(String line) {
      return Stream.of(line.trim().split(","))
          .map(String::trim)
          .map(s -> s.split(" "))
          .reduce(new Handful(0, 0, 0), Handful::build, Handful::combineHandfuls);
    }

    private static Handful build(Handful handful, String[] p) {
      var qty = Integer.parseInt(p[0]);
      var color = p[1].trim();
      return switch (color) {
        case "red" -> new Handful(handful.red() + qty, handful.green(), handful.blue());
        case "green" -> new Handful(handful.red(), handful.green() + qty, handful.blue());
        case "blue" -> new Handful(handful.red(), handful.green(), handful.blue() + qty);
        default -> throw new IllegalStateException("Unexpected value: " + color);
      };
    }

    private static Handful combineHandfuls(Handful h1, Handful h2) {
      return new Handful(h1.red() + h2.red(), h1.green() + h2.green(), h1.blue() + h2.blue());
    }

    public Handful max(Handful other) {
      return new Handful(
          Math.max(this.red(), other.red()),
          Math.max(this.green(), other.green()),
          Math.max(this.blue(), other.blue()));
    }
  }
}
