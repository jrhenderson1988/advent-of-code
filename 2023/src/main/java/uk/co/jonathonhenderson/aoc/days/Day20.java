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
    // https://dreampuf.github.io/GraphvizOnline/#digraph%20G%20%7B%0A%0Ahs%20-%3E%20sl%0Adg%20-%3E%20rx%0Avp%20-%3E%20fd%2C%20dv%0Akz%20-%3E%20jc%2C%20mc%0Anv%20-%3E%20dv%0Ahx%20-%3E%20gf%0Amm%20-%3E%20vh%0Afd%20-%3E%20td%0Adv%20-%3E%20hx%2C%20bl%2C%20rc%2C%20fd%2C%20xt%0Ahg%20-%3E%20xq%0Atd%20-%3E%20dv%2C%20hx%0Abl%20-%3E%20jt%0Abr%20-%3E%20jq%0Aqh%20-%3E%20ln%0Axq%20-%3E%20zl%2C%20cx%2C%20qh%2C%20hs%2C%20nt%2C%20sp%0Asg%20-%3E%20vv%2C%20tr%0Adm%20-%3E%20bl%2C%20dv%0Agt%20-%3E%20xq%2C%20hg%0Aln%20-%3E%20mq%2C%20xq%0Amc%20-%3E%20xv%2C%20jc%0Atx%20-%3E%20rv%2C%20jc%0Alk%20-%3E%20dg%0Amg%20-%3E%20hl%2C%20jc%0Avv%20-%3E%20zv%2C%20br%2C%20kx%2C%20mm%2C%20tr%0Ant%20-%3E%20xq%2C%20cx%0Azv%20-%3E%20dg%0Acd%20-%3E%20jc%2C%20ps%0Arc%20-%3E%20rm%2C%20dv%0Anj%20-%3E%20pt%2C%20xq%0Abroadcaster%20-%3E%20nt%2C%20kx%2C%20rc%2C%20mg%0Agf%20-%3E%20dc%2C%20dv%0Arm%20-%3E%20dm%2C%20dv%0Axx%20-%3E%20vv%2C%20cz%0Ajt%20-%3E%20dv%2C%20vp%0Azl%20-%3E%20nj%0Asp%20-%3E%20dg%0Axc%20-%3E%20jc%2C%20kz%0Axt%20-%3E%20dg%0Atp%20-%3E%20jc%0Alc%20-%3E%20vv%2C%20vn%0Avh%20-%3E%20xx%2C%20vv%0Amq%20-%3E%20hs%2C%20xq%0Acc%20-%3E%20vv%0Avn%20-%3E%20vv%2C%20cc%0Atr%20-%3E%20br%0Ahl%20-%3E%20qb%2C%20jc%0Adc%20-%3E%20dv%2C%20nv%0Ajq%20-%3E%20mm%2C%20vv%0Akx%20-%3E%20vv%2C%20sg%0Acx%20-%3E%20qh%0Asl%20-%3E%20zl%2C%20xq%0Acz%20-%3E%20lc%2C%20vv%0Aqb%20-%3E%20jc%2C%20cd%0Ajc%20-%3E%20ps%2C%20xv%2C%20lk%2C%20mg%0Axv%20-%3E%20tx%0Apt%20-%3E%20xq%2C%20gt%0Arv%20-%3E%20jc%2C%20tp%0Aps%20-%3E%20xc%0A%0A%0Adg%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Adv%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Axq%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Alk%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Avv%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Azv%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Asp%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Axt%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0Ajc%5Bstyle%3Dfilled%3B%20color%3Dlightgrey%5D%0A%0Arx%5Bstyle%3Dfilled%3Bcolor%3Dred%5D%0A%0A%0A%7D

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
