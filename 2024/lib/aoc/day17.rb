module Aoc
  class Day17 < Day
    def part1
      output = execute_program(create_registers)
      output.join(",")
    end

    def part2
      # 2,4 -> bst(4) -> B = A % 8
      # 1,1 -> bxl(1) -> B = B ^ 1
      # 7,5 -> cdv(5) -> C = A / (2 ^ B)
      # 0,3 -> adv(3) -> A = A / (2 ^ 3)
      # 1,4 -> bxl(4) -> B = B ^ 4
      # 4,4 -> bxc(4) -> B = B ^ C
      # 5,5 -> out(5) -> OUTPUT B % 8
      # 3,0 -> jnz(0) -> IF A == 0 HALT! ELSE IP = 0

      # Translates to:
      #
      # while a != 0
      #   B = A % 8
      #   B = B ^ 1
      #   C = A / (2 ^ B)
      #   A = A / (2 ^ 3)
      #   B = B ^ 4
      #   B = B ^ C
      #   OUTPUT B % 8

      # In Ruby (refactored):
      #
      # a = 30886132
      # b = 0
      # c = 0
      # output = []
      # while a != 0
      #   b = (a % 8) ^ 1
      #   c = a / (2 ** b)
      #   a = a / 8
      #   b = b ^ 4 ^ c
      #   output.append(b % 8)
      # end

      puts(output.join(","))

      # _, b, c = create_registers
      #
      # puts(program.length)
      #
      # # Observation: the output increases by 1 character when i reaches 8 times the previous
      # # time the output increased by 1 character. We could calculate the point to start based on
      # # the size of the program output (assuming the same happens in the real output).
      # #
      # # e.g.
      # # output size is 1 character long when i is 0
      # # output size is 2 characters long when i is 8
      # # output size is 3 characters long when i is 64
      # # output size is 4 characters long when i is 512
      # # output size is 5 characters long when i is 4096
      # # output size is 6 characters long when i is 32768
      # #
      # # Given the above, we could start at 8^{<desired output size> - 1}
      # # in this case, 8^(16-1) = 8^15 = 35184372088832
      # i = 8 ** (program.length - 1)
      #
      # curr_output_size = 0
      # current_first_char = nil
      # while true
      #   # puts(i)
      #   registers = [i, b, c]
      #   output = execute_program(registers)
      #   if output.length != curr_output_size
      #     curr_output_size = output.length
      #     puts("new output size: #{curr_output_size} (#{i})")
      #     if output[0] != current_first_char
      #       current_first_char = output[0]
      #       puts("first char changed: #{current_first_char} (#{i})")
      #     end
      #   end
      #
      #   if output == program
      #     return i
      #   end
      #   i += 1
      # end
      #
      # i
    end

    def execute_program(registers)
      ptr = 0
      output = []
      until halted?(ptr)
        op_code, operand = read_next_instruction(ptr)
        ptr, registers, output = eval_instruction(op_code, operand, ptr, registers, output)
        # puts("#{registers.inspect} -> #{output.inspect}")
      end

      output
    end

    def chunks
      @chunks ||= content.gsub(/\r/, "\n").split(/\n{2,}/)
    end

    def create_registers
      register_map =
        chunks.first
              .strip
              .lines
              .map { |line|
                vals = line.strip.match(/Register ([ABC]):\s*(\d+)/).captures
                [vals[0], vals[1].to_i]
              }
              .to_h

      [register_map["A"], register_map["B"], register_map["C"]]
    end

    def program
      @program ||=
        chunks.last
              .strip
              .match(/Program:\s*([0-9,]+)/)
              .captures
              .first
              .split(",")
              .map { |n| n.strip.to_i }
    end

    def read_next_instruction(instruction_pointer)
      op_code = program[instruction_pointer]
      operand = program[instruction_pointer + 1]

      [op_code, operand]
    end

    def halted?(instruction_pointer)
      instruction_pointer >= program.length
    end

    def eval_combo_operand(operand, registers)
      case operand
      when 0, 1, 2, 3
        operand
      when 4
        registers[0]
      when 5
        registers[1]
      when 6
        registers[2]
      else
        raise ArgumentError
      end
    end

    def eval_instruction(op_code, operand, instruction_pointer, registers, output)
      case op_code
      when 0
        adv(operand, instruction_pointer, registers, output)
      when 1
        bxl(operand, instruction_pointer, registers, output)
      when 2
        bst(operand, instruction_pointer, registers, output)
      when 3
        jnz(operand, instruction_pointer, registers, output)
      when 4
        bxc(operand, instruction_pointer, registers, output)
      when 5
        out(operand, instruction_pointer, registers, output)
      when 6
        bdv(operand, instruction_pointer, registers, output)
      when 7
        cdv(operand, instruction_pointer, registers, output)
      else
        raise ArgumentError, "invalid op code: #{op_code}"
      end
    end

    def adv(operand, instruction_pointer, registers, output)
      # The adv instruction (opcode 0) performs division. The numerator is the value in the A
      # register. The denominator is found by raising 2 to the power of the instruction's combo
      # operand. (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by
      # 2^B.) The result of the division operation is truncated to an integer and then written to
      # the A register.
      a, b, c = registers
      denominator = 2 ** eval_combo_operand(operand, registers)
      result = a / denominator

      [instruction_pointer + 2, [result, b, c], output]
    end

    def bxl(operand, instruction_pointer, registers, output)
      # The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the
      # instruction's literal operand, then stores the result in register B.
      a, b, c = registers
      result = b ^ operand

      [instruction_pointer + 2, [a, result, c], output]
    end

    def bst(operand, instruction_pointer, registers, output)
      # The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby
      # keeping only its lowest 3 bits), then writes that value to the B register.
      a, _, c = registers
      result = eval_combo_operand(operand, registers) % 8

      [instruction_pointer + 2, [a, result, c], output]
    end

    def jnz(operand, instruction_pointer, registers, output)
      # The jnz instruction (opcode 3) does nothing if the A register is 0. However, if the A
      # register is not zero, it jumps by setting the instruction pointer to the value of its
      # literal operand; if this instruction jumps, the instruction pointer is not increased by 2
      # after this instruction.
      a, b, c = registers
      if a == 0
        [instruction_pointer + 2, [a, b, c], output]
      else
        [operand, [a, b, c], output]
      end
    end

    def bxc(_, instruction_pointer, registers, output)
      # The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then
      # stores the result in register B. (For legacy reasons, this instruction reads an operand but
      # ignores it.)
      a, b, c = registers
      result = b ^ c

      [instruction_pointer + 2, [a, result, c], output]
    end

    def out(operand, instruction_pointer, registers, output)
      # The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then
      # outputs that value. (If a program outputs multiple values, they are separated by commas.)
      result = eval_combo_operand(operand, registers) % 8

      [instruction_pointer + 2, registers, output + [result]]
    end

    def bdv(operand, instruction_pointer, registers, output)
      # The bdv instruction (opcode 6) works exactly like the adv instruction except that the result
      # is stored in the B register. (The numerator is still read from the A register.)
      a, _, c = registers
      denominator = 2 ** eval_combo_operand(operand, registers)
      result = a / denominator

      [instruction_pointer + 2, [a, result, c], output]
    end

    def cdv(operand, instruction_pointer, registers, output)
      # The cdv instruction (opcode 7) works exactly like the adv instruction except that the result
      # is stored in the C register. (The numerator is still read from the A register.)
      a, b, _ = registers
      denominator = 2 ** eval_combo_operand(operand, registers)
      result = a / denominator

      [instruction_pointer + 2, [a, b, result], output]
    end

    def register_a(registers)
      registers[0]
    end

    def register_b(registers)
      registers[1]
    end

    def register_c(registers)
      registers[2]
    end

  end
end