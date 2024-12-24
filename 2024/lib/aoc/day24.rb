module Aoc
  class Day24 < Day
    GATE_PATTERN = /^([a-z\d]{3})\s*(AND|OR|XOR)\s*([a-z\d]{3})\s*->\s*([a-z\d]{3})$/

    def part1
      execute_system(gates, wires.clone)
    end

    def part2
      puts(system_correct?(gates).inspect)
      # puts(execute_system_with_inputs(gates, 12345, 12345))
      "TODO"
    end

    def system_correct?(gates)
      x = (0...total_x_wires).map { |_| "1" }.join("").to_i(2) # all the 1s
      y = (0...total_y_wires).map { |_| "1" }.join("").to_i(2) # all the 1s
      result = execute_system_with_inputs(gates, x, y)
      puts("expected: #{x + y}, actual: #{result}")
      x + y == result
      # pick some random numbers (maybe we should find out the bit size of the inputs and generate
      #   random numbers within those bit sizes)
      # execute the system (passing in the gates), with the two numbers
      # verify that the output of executing the system produces the same output as we get from
      #   adding the two numbers
      # maybe we can do this will a few different sets of random numbers
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
  end
end