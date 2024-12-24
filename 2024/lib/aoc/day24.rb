module Aoc
  class Day24 < Day
    GATE_PATTERN = /^([a-z\d]{3})\s*(AND|OR|XOR)\s*([a-z\d]{3})\s*->\s*([a-z\d]{3})$/

    def part1
      w = wires.clone
      until all_z_wires_have_values(w)
        w = exec_gates(gates, w)
      end

      to_decimal(w)
    end

    def part2
      "TODO"
    end

    def inputs
      @inputs ||= chunks.first.strip.lines.map { |line| parse_input(line) }.to_h
    end

    def parse_input(line)
      wire, value = line.strip.split(":").map { |part| part.strip }
      [wire, value.to_i == 1]
    end

    def gates
      @gates ||= chunks.last.strip.lines.map { |line| parse_gate(line) }
    end

    def wires
      @wires ||=
        gates.flat_map { |gate| [gate[0], gate[2], gate[3]] }
             .map { |input| [input, inputs[input]] }
             .to_h
    end

    def all_z_wires_have_values(wires)
      wires.filter { |k, _| k.start_with?("z") }.all? { |_, v| !v.nil? }
    end

    def exec_gates(gates, wires)
      gates.each { |gate|
        a, op, b, out = gate
        if wires[out].nil? && !wires[a].nil? && !wires[b].nil?
          wires[out] = exec_gate(wires[a], op, wires[b])
        end
      }

      wires
    end

    def exec_gate(a, op, b)
      case op
      when "OR"
        a || b
      when "AND"
        a && b
      when "XOR"
        a ^ b
      else
        raise ArgumentError, "invalid op"
      end
    end

    def parse_gate(line)
      a, op, b, out = line.strip.match(GATE_PATTERN).captures
      [a, op, b, out]
    end

    def to_decimal(wires)
      wires.filter { |k, _| k.start_with?("z") }
           .sort_by { |k, _| k[1..].to_i }
           .map { |kv| kv[1] ? "1" : "0" }
           .reverse
           .join("")
           .to_i(2)
    end
  end
end