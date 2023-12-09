package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class Day09 extends Day {

  private final OasisReport oasisReport;

  public Day09(String input) {
    this.oasisReport = OasisReport.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(oasisReport.sumOfExtrapolatedValues());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private record OasisReport(List<List<Long>> readings) {
    public static OasisReport parse(String input) {
      return new OasisReport(input.trim().lines().map(OasisReport::parseLine).toList());
    }

    private static List<Long> parseLine(String line) {
      return Arrays.stream(line.trim().split("\\s+"))
          .map(String::trim)
          .map(Long::parseLong)
          .toList();
    }

    public long sumOfExtrapolatedValues() {
      return readings.stream().map(this::extrapolate).reduce(Long::sum).orElseThrow();
    }

    private long extrapolate(List<Long> reading) {
      var layers = buildLayers(reading);
      var nextDiff = nextDifference(layers);

      return reading.get(reading.size() - 1) + nextDiff;
    }

    private long nextDifference(List<List<Long>> layers) {
      if (layers.size() == 1) {
        return 0;
      }

      var nextDiff = 0L;
      for (var i = layers.size() - 2; i >= 0; i--) {
        var layer = layers.get(i);
        var lastValue = layer.get(layer.size() - 1);
        nextDiff = lastValue + nextDiff;
      }

      return nextDiff;
    }

    private List<List<Long>> buildLayers(List<Long> reading) {
      var layers = new ArrayList<List<Long>>();

      var previous = reading;
      while (true) {
        var layer = differences(previous);
        layers.add(layer);
        if (layer.stream().allMatch(v -> v == 0)) {
          break;
        }
        previous = layer;
      }

      return layers;
    }

    private List<Long> differences(List<Long> input) {
      var differences = new ArrayList<Long>();
      for (var i = 0; i < input.size() - 1; i++) {
        differences.add(input.get(i + 1) - input.get(i));
      }
      return differences;
    }
  }
}
