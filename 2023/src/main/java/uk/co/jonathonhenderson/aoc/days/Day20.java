package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import uk.co.jonathonhenderson.aoc.common.Pair;

public class Day20 extends Day {
  private final String input;

  public Day20(String input) {
    this.input = input;
  }

  @Override
  public Optional<String> part1() {
    return answer(
        ModuleConfiguration.parse(input).productOfLowAndHighPulsesSentAfterPresses(1000L));
  }

  @Override
  public Optional<String> part2() {
    return answer(
        ModuleConfiguration.parse(input).totalPressesRequiredToSendLowPulseToRx());
  }

  private enum Pulse {
    HIGH,
    LOW
  }

  private sealed interface Module {
    static Module parse(String line) {
      var parts = line.trim().split("->");
      var first = parts[0].trim();
      var typeChar = first.charAt(0);
      var targets = Arrays.stream(parts[1].trim().split(",")).map(String::trim).toList();
      return switch (typeChar) {
        case '%' -> new FlipFlop(first.substring(1), targets, false);
        case '&' -> new Conjunction(first.substring(1), targets);
        case 'b' -> new Broadcaster(first, targets);
        default -> throw new IllegalArgumentException("Invalid type");
      };
    }

    String name();

    List<String> targets();

    List<Message> receive(Message message);

    void setSources(List<String> sources);

    final class FlipFlop implements Module {
      private final String name;
      private final List<String> targets;
      private boolean on;

      public FlipFlop(String name, List<String> targets, boolean on) {
        this.name = name;
        this.targets = targets;
        this.on = on;
      }

      @Override
      public String name() {
        return this.name;
      }

      @Override
      public List<String> targets() {
        return targets;
      }

      @Override
      public List<Message> receive(Message message) {
        if (message.pulse().equals(Pulse.HIGH)) {
          return List.of();
        } else if (message.pulse().equals(Pulse.LOW)) {
          var messages =
              targets.stream()
                  .map(target -> Message.of(name, target, on ? Pulse.LOW : Pulse.HIGH))
                  .toList();
          this.on = !this.on;
          return messages;
        }

        throw new IllegalStateException("Unreachable");
      }

      @Override
      public void setSources(List<String> sources) {
        // not needed
      }
    }

    final class Conjunction implements Module {
      private final String name;
      private final List<String> targets;
      private final Map<String, Pulse> memory;
      private List<String> sources;

      public Conjunction(String name, List<String> targets) {
        this.name = name;
        this.targets = targets;
        this.memory = new HashMap<>();
      }

      @Override
      public String name() {
        return name;
      }

      @Override
      public List<String> targets() {
        return targets;
      }

      @Override
      public List<Message> receive(Message message) {
        memory.put(message.sender(), message.pulse());

        var pulse =
            sources.stream()
                    .map(source -> memory.getOrDefault(source, Pulse.LOW))
                    .allMatch(p -> p.equals(Pulse.HIGH))
                ? Pulse.LOW
                : Pulse.HIGH;

        return targets.stream().map(target -> Message.of(name, target, pulse)).toList();
      }

      @Override
      public void setSources(List<String> sources) {
        this.sources = sources;
      }
    }

    final class Broadcaster implements Module {
      private final String name;
      private final List<String> targets;

      public Broadcaster(String name, List<String> targets) {
        this.name = name;
        this.targets = targets;
      }

      @Override
      public String name() {
        return name;
      }

      @Override
      public List<String> targets() {
        return targets;
      }

      @Override
      public List<Message> receive(Message message) {
        return targets.stream().map(target -> Message.of(name, target, Pulse.LOW)).toList();
      }

      @Override
      public void setSources(List<String> sources) {
        // not needed
      }
    }
  }

  private record Message(String sender, String destination, Pulse pulse) {
    public static Message of(String sender, String destination, Pulse pulse) {
      return new Message(sender, destination, pulse);
    }
  }

  private record ModuleConfiguration(Map<String, Module> modules) {
    public static ModuleConfiguration parse(String input) {
      var config =
          wireUpSources(
              input
                  .trim()
                  .lines()
                  .map(Module::parse)
                  .collect(Collectors.toMap(Module::name, m -> m)));

      return new ModuleConfiguration(config);
    }

    private static Map<String, Module> wireUpSources(Map<String, Module> config) {
      for (var target : config.keySet()) {
        var sources = new ArrayList<String>();
        for (var module : config.values()) {
          if (module.targets().contains(target)) {
            sources.add(module.name());
          }
        }
        config.get(target).setSources(sources);
      }

      return config;
    }

    public Pair<Long, Long> simulateButtonPress() {
      long totalLowSent = 0L;
      long totalHighSent = 0L;

      var messages = List.of(Message.of("*", "broadcaster", Pulse.LOW));
      while (!messages.isEmpty()) {
        var newMessages = new ArrayList<Message>();
        for (var msg : messages) {
          if (msg.pulse().equals(Pulse.HIGH)) {
            totalHighSent++;
          } else if (msg.pulse().equals(Pulse.LOW)) {
            totalLowSent++;
          }

          var dest = modules.get(msg.destination());
          if (dest == null) {
            // pulse does not go anywhere
            continue;
          }

          var furtherMessages = dest.receive(msg);
          newMessages.addAll(furtherMessages);
        }
        messages = newMessages;
      }

      return Pair.of(totalHighSent, totalLowSent);
    }

    public long productOfLowAndHighPulsesSentAfterPresses(long presses) {
      var totalLow = 0L;
      var totalHigh = 0L;
      for (var i = 0L; i < presses; i++) {
        var result = simulateButtonPress();
        totalHigh += result.left();
        totalLow += result.right();
      }
      return totalLow * totalHigh;
    }

    public long totalPressesRequiredToSendLowPulseToRx() {
      var total = 0L;

      while (true) {
        total++;

        var messages = List.of(Message.of("*", "broadcaster", Pulse.LOW));
        while (!messages.isEmpty()) {
          var newMessages = new ArrayList<Message>();
          for (var msg : messages) {
            if (msg.pulse().equals(Pulse.LOW) && msg.destination().equals("rx")) {
              return total;
            }

            var dest = modules.get(msg.destination());
            if (dest == null) {
              // pulse does not go anywhere
              continue;
            }

            var furtherMessages = dest.receive(msg);
            newMessages.addAll(furtherMessages);
          }
          messages = newMessages;
        }
      }
    }
  }
}
