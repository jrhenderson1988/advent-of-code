package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class Day15 extends Day {

  private final List<String> initialisationSequence;

  public Day15(String input) {
    this.initialisationSequence = Arrays.stream(input.trim().split(",")).map(String::trim).toList();
  }

  @Override
  public Optional<String> part1() {
    return answer(sumOfHashingInitialisationSequence());
  }

  @Override
  public Optional<String> part2() {
    return answer(focusingPowerAfterRunningInitialisationSequence());
  }

  private int sumOfHashingInitialisationSequence() {
    return initialisationSequence.stream().map(this::hash).reduce(Integer::sum).orElseThrow();
  }

  private int focusingPowerAfterRunningInitialisationSequence() {
    var boxes = new HashMap<Integer, List<Lens>>();
    for (var entry : initialisationSequence) {
      var instruction = Instruction.parse(entry);
      var boxId = hash(instruction.label());
      var contents = boxes.getOrDefault(boxId, List.of());
      boxes.put(boxId, updateContents(instruction, contents));
    }

    return calculateFocusingPower(boxes);
  }

  private List<Lens> updateContents(Instruction instruction, List<Lens> existing) {
    return switch (instruction.operation()) {
      case DASH -> removeLensWithLabel(existing, instruction.label());
      case EQUALS -> lensWithLabelExists(existing, instruction.label())
          ? replaceLens(existing, Lens.of(instruction.label(), instruction.value()))
          : appendLens(existing, Lens.of(instruction.label(), instruction.value()));
    };
  }

  private List<Lens> removeLensWithLabel(List<Lens> existing, String label) {
    return existing.stream().filter(lens -> !lens.label().equals(label)).toList();
  }

  private boolean lensWithLabelExists(List<Lens> existing, String label) {
    return existing.stream().anyMatch(lens -> lens.label().equals(label));
  }

  private List<Lens> replaceLens(List<Lens> existing, Lens newLens) {
    return existing.stream()
        .map(lens -> lens.label().equals(newLens.label()) ? newLens : lens)
        .toList();
  }

  private List<Lens> appendLens(List<Lens> existing, Lens lens) {
    var n = new ArrayList<>(existing);
    n.add(lens);
    return n;
  }

  private int calculateFocusingPower(Map<Integer, List<Lens>> boxes) {
    return boxes.entrySet().stream()
        .map(entry -> focusingPowerOfBox(entry.getKey(), entry.getValue()))
        .reduce(Integer::sum)
        .orElseThrow();
  }

  private int focusingPowerOfBox(int boxId, List<Lens> lenses) {
    if (lenses.isEmpty()) {
      return 0;
    }

    return IntStream.range(0, lenses.size())
        .mapToObj(slot -> focusingPowerOfLens(boxId, slot, lenses.get(slot).focalLength()))
        .reduce(Integer::sum)
        .orElseThrow();
  }

  private int focusingPowerOfLens(int boxId, int slotNumber, int focalLength) {
    return (boxId + 1) * (slotNumber + 1) * focalLength;
  }

  private int hash(String input) {
    var currentValue = 0;
    for (var ascii : input.chars().toArray()) {
      currentValue += ascii;
      currentValue *= 17;
      currentValue = currentValue % 256;
    }

    return currentValue;
  }

  private enum Operation {
    DASH,
    EQUALS
  }

  private record Lens(String label, int focalLength) {
    public static Lens of(String label, int focalLength) {
      return new Lens(label, focalLength);
    }

    @Override
    public String toString() {
      return "[" + label + " " + focalLength + "]";
    }
  }

  private record Instruction(String label, Operation operation, int value) {
    public static Instruction parse(String input) {
      var dashPos = input.indexOf('-');
      if (dashPos > -1) {
        var label = input.substring(0, dashPos).trim();
        return new Instruction(label, Operation.DASH, -1);
      }

      var eqPos = input.indexOf('=');
      if (eqPos > -1) {
        var label = input.substring(0, eqPos).trim();
        var value = Integer.parseInt(input.substring(eqPos + 1).trim());
        return new Instruction(label, Operation.EQUALS, value);
      }

      throw new IllegalArgumentException("Invalid instruction");
    }

    @Override
    public String toString() {
      return switch (operation()) {
        case DASH -> label + "-";
        case EQUALS -> label + "=" + value;
      };
    }
  }
}
