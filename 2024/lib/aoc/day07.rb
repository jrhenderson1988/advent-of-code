module Aoc
  class Day07 < Day
    ADD = '+'
    MUL = '*'
    OPERATORS = [ADD, MUL]

    def part1
      equations.filter { |equation| satisfiable?(equation) }
               .map { |equation| test_value(equation) }
               .sum
    end

    def part2
      "TODO"
    end

    def equations
      lines.map { |line| parse_equation(line) }
    end

    def parse_equation(equation)
      chunks = equation.split(/:\s*/)
      answer = chunks[0].strip.to_i
      numbers = chunks[1].split(/\s+/).map { |n| n.strip.to_i }

      [answer, numbers]
    end

    def satisfiable?(equation)
      test_value = test_value(equation)
      operands = operands(equation)
      operator_combinations = generate_operator_combinations(operands.length - 1)

      operator_combinations.any? { |operators| calculate_value(operands, operators) == test_value }
    end

    def test_value(equation)
      equation[0]
    end

    def operands(equation)
      equation[1]
    end

    def calculate_value(numbers, operations, total = nil)
      if total.nil?
        raise ArgumentError if numbers.length != operations.length + 1
        calculate_value(numbers[1..], operations, numbers[0])
      elsif numbers.length == 0 && operations.length == 0
        total
      else
        raise ArgumentError if numbers.length != operations.length
        op = operations[0]
        num = numbers[0]
        case op
        when ADD
          calculate_value(numbers[1..], operations[1..], total + num)
        when MUL
          calculate_value(numbers[1..], operations[1..], total * num)
        else
          raise ArgumentError
        end
      end
    end

    def generate_operator_combinations(size)
      max = (0..size - 1).map { |_| "1" }.to_a.join("").to_i(2)
      (0..max).map { |i| i.to_s(2).rjust(size, "0") }
              .map { |n| n.chars.map { |c| OPERATORS[c.to_i] } }
    end
  end
end