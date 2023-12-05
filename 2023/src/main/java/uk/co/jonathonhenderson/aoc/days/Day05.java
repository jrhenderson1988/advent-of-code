package uk.co.jonathonhenderson.aoc.days;

import static uk.co.jonathonhenderson.aoc.common.Lines.splitByEmptyLines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    return answer(almanac.findClosestLocationForSeedPairs());
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

  private static class Almanac {
    private final List<Long> seeds;
    private final List<Mapper> mappers;
    private final Map<Subject, Mapper> mappersFrom;
    private final Map<Subject, Mapper> mappersTo;

    public Almanac(List<Long> seeds, List<Mapper> mappers) {
      this.seeds = seeds;
      this.mappers = mappers;
      this.mappersFrom = mappers.stream().collect(Collectors.toMap(Mapper::from, m -> m));
      this.mappersTo = mappers.stream().collect(Collectors.toMap(Mapper::to, m -> m));
    }

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

    public long findClosestLocationForSeedPairs() {
      var seedRanges = new ArrayList<SeedRange>();
      for (var i = 0; i < this.seeds.size() - 1; i += 2) {
        var seed = seeds.get(i);
        var rangeLength = seeds.get(i + 1);
        seedRanges.add(new SeedRange(seed, rangeLength));
      }

      var location = 0L;
      while (true) {
        var seed = findSeedForLocation(location);
        for (var r : seedRanges) {
          if (r.contains(seed)) {
            return location;
          }
        }
        location++;
      }
    }

    public long findClosestLocationForSeeds() {
      return seeds.stream().map(this::findLocationOfSeed).reduce(Long::min).orElseThrow();
    }

    private long findSeedForLocation(long location) {
      var mapper = findMapperTo(Subject.LOCATION);
      var nextSubject = mapper.from();

      long value = location;
      while (nextSubject != Subject.SEED) {
        value = mapper.toSource(value);

        mapper = findMapperTo(nextSubject);
        nextSubject = mapper.from();
      }

      return mapper.toSource(value);
    }

    private long findLocationOfSeed(long seed) {
      var mapper = findMapperFrom(Subject.SEED);
      var nextSubject = mapper.to();

      long value = seed;
      while (nextSubject != Subject.LOCATION) {
        value = mapper.toDestination(value);

        mapper = findMapperFrom(nextSubject);
        nextSubject = mapper.to();
      }

      return mapper.toDestination(value);
    }

    private Mapper findMapperFrom(Subject subject) {
      return mappersFrom.get(subject);
    }

    private Mapper findMapperTo(Subject subject) {
      return mappersTo.get(subject);
    }
  }

  private record MappingRange(long destinationStart, long sourceStart, long length) {
    public static MappingRange parse(String line) {
      var values = Stream.of(line.trim().split("\\s+")).map(Long::parseLong).toList();
      if (values.size() != 3) {
        throw new IllegalArgumentException("Invalid range - expected 3 numbers");
      }
      return new MappingRange(values.get(0), values.get(1), values.get(2));
    }

    public Optional<Long> toDestination(long input) {
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

    public Optional<Long> toSource(long input) {
      if (length == 0) {
        return Optional.empty();
      }

      if (input < destinationStart) {
        return Optional.empty();
      }

      if (input > destinationStart + (length - 1)) {
        return Optional.empty();
      }

      return Optional.of(sourceStart + (input - destinationStart));
    }
  }

  private record Mapper(Subject from, Subject to, List<MappingRange> ranges) {
    public static Mapper parse(Pair<String, String> input) {
      var name = input.left().trim().split("map")[0].trim();
      var parts = name.split("-to-");
      var from = Subject.from(parts[0].trim());
      var to = Subject.from(parts[1].trim());
      var ranges = input.right().trim().lines().map(MappingRange::parse).toList();
      return new Mapper(from, to, ranges);
    }

    public long toDestination(long input) {
      return ranges.stream()
          .map(range -> range.toDestination(input))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst()
          .orElse(input);
    }

    public long toSource(long input) {
      return ranges.stream()
          .map(range -> range.toSource(input))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst()
          .orElse(input);
    }
  }

  private record SeedRange(long start, long length) {
    public boolean contains(long n) {
      if (length == 0) {
        return false;
      }
      if (n < start) {
        return false;
      }

      if (n > start + (length - 1)) {
        return false;
      }

      return true;
    }
  }
}
