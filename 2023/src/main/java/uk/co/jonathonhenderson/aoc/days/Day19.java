package uk.co.jonathonhenderson.aoc.days;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import uk.co.jonathonhenderson.aoc.common.Lines;
import uk.co.jonathonhenderson.aoc.common.Pair;

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
    return answer(workflows.totalDistinctCombinationsThatWouldBeAccepted());
  }

  private enum Op {
    GT,
    LT,
    GTE,
    LTE;

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
        default -> throw new IllegalArgumentException("Cannot apply");
      };
    }

    public Op compliment() {
      return switch (this) {
        case GT -> LTE;
        case LT -> GTE;
        case GTE -> LT;
        case LTE -> GT;
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

      public Operation compliment() {
        return new Operation(identifier, op.compliment(), value, outcome);
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

  private record RangePart(
      Pair<Long, Long> x, Pair<Long, Long> m, Pair<Long, Long> a, Pair<Long, Long> s) {
    public static RangePart of(
        Pair<Long, Long> x, Pair<Long, Long> m, Pair<Long, Long> a, Pair<Long, Long> s) {
      return new RangePart(x, m, a, s);
    }

    public boolean isValid() {
      return x.left() <= x.right()
          && m.left() <= m.right()
          && a.left() <= a.right()
          && s.left() <= s.right();
    }

    public long value() {
      return (x.right() - x.left() + 1)
          * (m.right() - m.left() + 1)
          * (a.right() - a.left() + 1)
          * (s.right() - s.left() + 1);
    }
  }

  private record State(String name, RangePart part) {
    public static State of(String name, RangePart part) {
      return new State(name, part);
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

    public long totalDistinctCombinationsThatWouldBeAccepted() {
      var total = 0L;
      var q = new ArrayDeque<State>();
      q.add(
          State.of(
              "in",
              RangePart.of(
                  Pair.of(1L, 4000L), Pair.of(1L, 4000L), Pair.of(1L, 4000L), Pair.of(1L, 4000L))));
      while (!q.isEmpty()) {
        var state = q.removeLast();
        var name = state.name();
        var part = state.part();

        if (!part.isValid()) {
          continue;
        }

        if (name.equals("A")) {
          total += part.value();
        } else if (!name.equals("R")) {
          var workflow = workflows.get(name);
          for (var rule : workflow.rules()) {
            if (rule instanceof Rule.Operation operation) {
              var res =
                  switch (operation.outcome()) {
                    case Outcome.Accept accept -> "A";
                    case Outcome.Reject reject -> "R";
                    case Outcome.NextWorkflow nwf -> nwf.name();
                  };

              q.addLast(State.of(res, newRanges(operation, part)));
              part = newRanges(operation.compliment(), part);
            } else {
              var res =
                  switch (rule) {
                    case Rule.Accept accept -> "A";
                    case Rule.Reject reject -> "R";
                    case Rule.NextWorkflow nwf -> nwf.name();
                    default -> throw new IllegalArgumentException("...");
                  };
              q.addLast(State.of(res, part));
              break;
            }
          }
        }
      }
      return total;
    }

    private Pair<Long, Long> newRange(Op op, long n, Pair<Long, Long> range) {
      return switch (op) {
        case Op.GT -> Pair.of(Math.max(range.left(), n + 1), range.right());
        case Op.LT -> Pair.of(range.left(), Math.min(range.right(), n - 1));
        case Op.GTE -> Pair.of(Math.max(range.left(), n), range.right());
        case Op.LTE -> Pair.of(range.left(), Math.min(range.right(), n));
      };
    }

    private RangePart newRanges(Rule.Operation operation, RangePart p) {
      var op = operation.op();
      var value = operation.value();
      return switch (operation.identifier()) {
        case 'x' -> RangePart.of(newRange(op, value, p.x()), p.m(), p.a(), p.s());
        case 'm' -> RangePart.of(p.x(), newRange(op, value, p.m()), p.a(), p.s());
        case 'a' -> RangePart.of(p.x(), p.m(), newRange(op, value, p.a()), p.s());
        case 's' -> RangePart.of(p.x(), p.m(), p.a(), newRange(op, value, p.s()));
        default -> p;
      };
    }
  }
}
