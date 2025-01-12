require 'set'

module Aoc
  class Day24 < Day
    GATE_PATTERN = /^([a-z\d]{3})\s*(AND|OR|XOR)\s*([a-z\d]{3})\s*->\s*([a-z\d]{3})$/

    def part1
      execute_system(gates, wires.clone)
    end

    def part2
      find_wires_to_swap.sort.join(",")
    end

    def find_wires_to_swap
      inputs = test? ? find_bad_output_wires_and(gates) : find_bad_wires_ripple_carry_adder(gates)

      inputs.sort
    end

    def find_bad_output_wires_and(gates)
      total_wires_to_find = 4
      max_x = 2 ** total_x_wires - 1
      max_y = 2 ** total_y_wires - 1

      bad_outputs = Set[]
      for _ in 0...1000
        x = Random.rand(max_x + 1) # random value
        y = Random.rand(max_y + 1) # random value
        result = execute_system_with_inputs(gates, x, y)
        expected = x & y
        if expected != result
          actual_bits_reversed = to_binary(result, total_z_wires).chars.reverse
          expected_bits_reversed = to_binary(expected, total_z_wires).chars.reverse
          (0...actual_bits_reversed.length)
            .filter { |i| actual_bits_reversed[i] != expected_bits_reversed[i] }
            .map { |i| "z" + i.to_s.rjust(2, "0") }
            .each { |bad_z| bad_outputs.add(bad_z) }

          if bad_outputs.length > total_wires_to_find
            raise ArgumentError, "too many bad output wires"
          elsif bad_outputs.length == total_wires_to_find
            return bad_outputs
          end
        end
      end

      raise ArgumentError, "couldn't find bad output wires - current: #{bad_outputs.inspect}"
    end

    def find_bad_wires_ripple_carry_adder(gates)
      # System is a Ripple Carry Adder (https://en.wikipedia.org/wiki/Adder_(electronics)#/media/File:RippleCarry2.gif)
      # - The first operation (on x0 and y0) is a half adder and the rest are full adders

      # Half adder (https://en.wikipedia.org/wiki/Adder_(electronics)#/media/File:Halfadder.gif):
      # - Accepts two inputs A and B
      # - The SUM is produced by A XOR B
      # - The CARRY is produced by A AND B

      # Full adder (https://en.wikipedia.org/wiki/Adder_(electronics)#/media/File:Fulladder.gif):
      # - Accepts two inputs A and B as well as a CARRY (from the previous adder)
      # - The SUM is produced by running A XOR B XOR CARRY
      # - The output CARRY is produced by running ((A XOR B) AND CARRY) OR (X AND B)

      bad = Set[]
      gates.each { |gate|
        a, op, b, out = gate

        # if the output is a z but the operation is not XOR (excluding the last output since it is
        # formed by the last carry)
        if (z?(out) && !xor?(op)) && !last_z?(out)
          bad.add(out)
        end

        # Every XOR operation should involve at least an input (x or y) or an output (z). If the
        # instruction doesn't involve an input or output, then its output wire is bad. In the first
        # circuit (a half-adder) the two inputs (x and y) are XOR'd to produce an output (z). In
        # subsequent circuits (full-adders) the two inputs (x and y) are XOR'd to produce an
        # intermediate value (neither x, y or z) which is subsequently XOR'd with the carry from the
        # previous circuit to produce an output (z). No other use of XOR is allowed.
        if xor?(op) && !x_y_or_z?(out) && !x_y_or_z?(a) && !x_y_or_z?(b)
          bad.add(out)
        end

        # an AND must be followed by an OR unless we're looking at the first inputs (since the first
        # is a half-adder where the carry flows into the following full-adder, and is eventually
        # part of an XOR operation)
        if and?(op) && ![a, b].any? { |v| first_x?(v) || first_y?(v) }
          next_circuit = find_next_circuit(gates, out)
          unless next_circuit.nil?
            _, sub_op, _, _ = next_circuit
            unless or?(sub_op)
              bad.add(out)
            end
          end
        end

        # an XOR must be followed by either an AND or another XOR, meaning that OR may not follow.
        if xor?(op)
          next_circuit = find_next_circuit(gates, out)
          unless next_circuit.nil?
            _, sub_op, _, _ = next_circuit
            if or?(sub_op)
              bad.add(out)
            end
          end
        end
      }

      bad
    end

    def execute_system_with_inputs(gates, x, y)
      w = set_x(wires.clone, x)
      w = set_y(w, y)

      execute_system(gates, w)
    end

    def execute_system(gates, wires)
      until all_z_wires_have_values(wires)
        wires = exec_gates(gates, wires)
      end

      to_decimal(wires)
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

    def total_x_wires
      @total_x_wires ||= wires.keys.filter { |k| k.match?(/^x\d\d$/) }.length
    end

    def total_y_wires
      @total_y_wires ||= wires.keys.filter { |k| k.match?(/^y\d\d$/) }.length
    end

    def total_z_wires
      @total_z_wires ||= wires.keys.filter { |k| k.match?(/^z\d\d$/) }.length
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
        a | b
      when "AND"
        a & b
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

    def to_binary(n, bits)
      output = n.to_s(2).rjust(bits, "0")
      if output.length != bits
        raise ArgumentError, "does not fit into #{bits} bits"
      end
      output
    end

    def set_x(wires, value)
      binary = to_binary(value, total_x_wires)
      charges = binary.chars.reverse.map { |c| c.to_i == 1 }
      (0...total_x_wires).each { |idx| wires["x#{idx.to_s.rjust(2, "0")}"] = charges[idx] }
      wires
    end

    def set_y(wires, value)
      binary = to_binary(value, total_y_wires)
      charges = binary.chars.reverse.map { |c| c.to_i == 1 }
      (0...total_y_wires).each { |idx| wires["y#{idx.to_s.rjust(2, "0")}"] = charges[idx] }
      wires
    end

    def x?(v)
      v.match?(/^x\d\d$/)
    end

    def y?(v)
      v.match?(/^y\d\d$/)
    end

    def z?(v)
      v.match?(/^z\d\d$/)
    end

    def x_y_or_z?(v)
      x?(v) || y?(v) || z?(v)
    end

    def xor?(v)
      v == "XOR"
    end

    def and?(v)
      v == "AND"
    end

    def or?(v)
      v == "OR"
    end

    def last_z?(v)
      v == "z#{total_z_wires - 1}"
    end

    def first_x?(v)
      v == "x00"
    end

    def first_y?(v)
      v == "y00"
    end

    def find_next_circuit(gates, out)
      gates.find { |gate| gate[0] == out || gate[2] == out }
    end
  end
end