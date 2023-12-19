package uk.co.jonathonhenderson.aoc.days;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import uk.co.jonathonhenderson.aoc.common.Lines;

public class Day19 extends Day {

  private final Workflows workflows;

  public Day19(String input) {
    this.workflows = Workflows.parse(input);
  }

  @Override
  public Optional<String> part1() {
    return answer(workflows.sumOfRatings());
  }

  @Override
  public Optional<String> part2() {
    return answer();
  }

  private enum Op {
    GT,
    LT;

    public static Op of(char c) {
      return switch (c) {
        case '>' -> GT;
        case '<' -> LT;
        default -> throw new IllegalArgumentException("Unrecognised op code");
      };
    }

    public boolean apply(long a, long b) {
      return switch (this) {
        case GT -> a > b;
        case LT -> a < b;
      };
    }
  }

  private sealed interface Outcome {
    static Outcome parse(String input) {
      var r = input.trim();
      return switch (input.trim()) {
        case "A" -> new Accept();
        case "R" -> new Reject();
        default -> new NextWorkflow(r);
      };
    }

    record Accept() implements Outcome {}

    record Reject() implements Outcome {}

    record NextWorkflow(String name) implements Outcome {}
  }

  private sealed interface Rule {
    static Rule parse(String input) {
      return switch (input.trim()) {
        case "A" -> new Accept();
        case "R" -> new Reject();
        default -> input.contains(":") ? Operation.parse(input) : new NextWorkflow(input);
      };
    }

    default boolean matches(Part part) {
      return true;
    }

    record Operation(char identifier, Op op, long value, Outcome outcome) implements Rule {
      public static Operation parse(String input) {
        var parts = input.trim().split(":");
        var outcome = Outcome.parse(parts[1]);

        var op = Op.of(parts[0].charAt(1));
        var identifier = parts[0].charAt(0);
        var value = Long.parseLong(parts[0].substring(2));
        return new Operation(identifier, op, value, outcome);
      }

      @Override
      public boolean matches(Part part) {
        var valueOfIdentifier = getValueOfIdentifier(part);
        return op.apply(valueOfIdentifier, value);
      }

      private long getValueOfIdentifier(Part part) {
        return switch (identifier) {
          case 'x' -> part.x();
          case 'm' -> part.m();
          case 'a' -> part.a();
          case 's' -> part.s();
          default -> throw new IllegalArgumentException("Unknown identifier");
        };
      }
    }

    record Accept() implements Rule {}

    record Reject() implements Rule {}

    record NextWorkflow(String name) implements Rule {}
  }

  private record Workflow(String name, List<Rule> rules) {

    public static Workflow parse(String line) {
      var bracePos = line.indexOf('{');
      var name = line.substring(0, bracePos);
      var rest = line.substring(bracePos + 1, line.length() - 1);
      var rules = Arrays.stream(rest.split(",")).map(Rule::parse).toList();
      return new Workflow(name, rules);
    }

    public Outcome getOutcome(Part part) {
      var rule = rules.stream().filter(r -> r.matches(part)).findFirst().orElseThrow();
      return switch (rule) {
        case Rule.Accept accept -> new Outcome.Accept();
        case Rule.NextWorkflow nextWorkflow -> new Outcome.NextWorkflow(nextWorkflow.name());
        case Rule.Operation operation -> operation.outcome();
        case Rule.Reject reject -> new Outcome.Reject();
      };
    }
  }

  private record Part(long x, long m, long a, long s) {
    public static Part parse(String input) {
      var values =
          Arrays.stream(input.trim().replace("{", "").replace("}", "").split(","))
              .map(p -> p.trim().split("="))
              .collect(Collectors.toMap(p -> p[0], p -> Long.parseLong(p[1])));
      return new Part(values.get("x"), values.get("m"), values.get("a"), values.get("s"));
    }

    public long getRating() {
      return x + m + a + s;
    }
  }

  private record Workflows(Map<String, Workflow> workflows, List<Part> parts) {

    public static Workflows parse(String input) {
      var chunks = Lines.splitByEmptyLines(input.trim()).toList();
      var workflowsChunk = chunks.getFirst();
      var partsChunk = chunks.getLast();

      return new Workflows(
          workflowsChunk
              .trim()
              .lines()
              .map(String::trim)
              .map(Workflow::parse)
              .collect(Collectors.toMap(Workflow::name, wf -> wf)),
          partsChunk.lines().map(Part::parse).toList());
    }

    public long sumOfRatings() {
      return parts.stream()
          .filter(this::applyWorkflows)
          .map(Part::getRating)
          .reduce(Long::sum)
          .orElseThrow();
    }

    private boolean applyWorkflows(Part part) {
      var curr = "in";
      while (true) {
        var wf = workflows.get(curr);
        var outcome = wf.getOutcome(part);
        if (outcome instanceof Outcome.Accept) {
          return true;
        } else if (outcome instanceof Outcome.Reject) {
          return false;
        }
        curr = ((Outcome.NextWorkflow) outcome).name();
      }
    }
  }
}
