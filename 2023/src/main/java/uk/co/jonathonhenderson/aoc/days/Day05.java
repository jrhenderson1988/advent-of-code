package uk.co.jonathonhenderson.aoc.days;

import static uk.co.jonathonhenderson.aoc.common.Lines.splitByEmptyLines;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import uk.co.jonathonhenderson.aoc.common.Pair;

public class Day05 extends Day {

  private final Almanac almanac;

  public Day05(String input) {
    this.almanac = Almanac.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(almanac.findClosestLocationForSeeds());
  }

  @Override
  public Optional<String> part2() {
    return nothing();
  }

  private enum Subject {
    SEED,
    SOIL,
    FERTILIZER,
    WATER,
    LIGHT,
    TEMPERATURE,
    HUMIDITY,
    LOCATION;

    public static Subject from(String s) {
      return Subject.valueOf(s.trim().toUpperCase());
    }
  }

  private record Almanac(List<Long> seeds, List<Mapper> mappers) {
    public static Almanac parse(String input) {
      var chunks = splitByEmptyLines(input).map(Almanac::parseChunk).toList();
      var seeds =
          Stream.of(
                  chunks.stream()
                      .filter(p -> p.left().equals("seeds"))
                      .findFirst()
                      .map(Pair::right)
                      .orElseThrow()
                      .split("\\s+"))
              .map(Long::parseLong)
              .toList();
      var mappers =
          chunks.stream().filter(p -> !p.left().equals("seeds")).map(Mapper::parse).toList();

      return new Almanac(seeds, mappers);
    }

    private static Pair<String, String> parseChunk(String input) {
      var parts = input.split(":");
      var label = parts[0].trim();
      var data = parts[1].trim();
      return Pair.of(label, data);
    }

    public long findClosestLocationForSeeds() {
      return seeds.stream().map(this::findLocationOfSeed).reduce(Long::min).orElseThrow();
    }

    private long findLocationOfSeed(long seed) {
      var mapper = findMapper(Subject.SEED);
      var nextSubject = mapper.to();

      long value = seed;
      while (nextSubject != Subject.LOCATION) {
        value = mapper.valueOf(value);

        mapper = findMapper(nextSubject);
        nextSubject = mapper.to();
      }

      return mapper.valueOf(value);
    }

    private Mapper findMapper(Subject subject) {
      return mappers.stream().filter(m -> m.from().equals(subject)).findFirst().orElseThrow();
    }
  }

  private record Range(long destinationStart, long sourceStart, long length) {
    public static Range parse(String line) {
      var values = Stream.of(line.trim().split("\\s+")).map(Long::parseLong).toList();
      if (values.size() != 3) {
        throw new IllegalArgumentException("Invalid range - expected 3 numbers");
      }
      return new Range(values.get(0), values.get(1), values.get(2));
    }

    public Optional<Long> valueOf(long input) {
      if (length == 0) {
        return Optional.empty();
      }

      if (input < sourceStart) {
        return Optional.empty();
      }

      if (input > sourceStart + (length - 1)) {
        return Optional.empty();
      }

      return Optional.of(destinationStart + (input - sourceStart));
    }
  }

  private record Mapper(Subject from, Subject to, List<Range> ranges) {
    public static Mapper parse(Pair<String, String> input) {
      var name = input.left().trim().split("map")[0].trim();
      var parts = name.split("-to-");
      var from = Subject.from(parts[0].trim());
      var to = Subject.from(parts[1].trim());
      var ranges = input.right().trim().lines().map(Range::parse).toList();
      return new Mapper(from, to, ranges);
    }

    public long valueOf(long input) {
      return ranges.stream()
          .map(range -> range.valueOf(input))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst()
          .orElse(input);
    }
  }
}
