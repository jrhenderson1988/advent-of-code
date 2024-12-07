module Aoc
  class Day07 < Day
    ADD = '+'
    MUL = '*'
    CONCAT = '|'

    def part1
      equations.filter { |equation| satisfiable?(equation, [ADD, MUL]) }
               .map { |equation| test_value(equation) }
               .sum
    end

    def part2
      equations.filter { |equation| satisfiable?(equation, [ADD, MUL, CONCAT]) }
               .map { |equation| test_value(equation) }
               .sum
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

    def satisfiable?(equation, operators)
      test_value = test_value(equation)
      operands = operands(equation)
      operator_combinations = generate_operator_combinations(operands.length - 1, operators)

      operator_combinations.any? { |ops| calculate_value(operands, ops) == test_value }
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
        calculate_value(numbers[1..], operations[1..], apply_operation(total, numbers[0], operations[0]))
      end
    end

    def generate_operator_combinations(size, operators)
      max = (0..size - 1).map { |_| operators.size - 1 }
                         .to_a
                         .join("")
                         .to_i(operators.length)

      (0..max).map { |i| i.to_s(operators.length).rjust(size, "0") }
              .map { |n| n.chars.map { |c| operators[c.to_i] } }
    end

    def apply_operation(a, b, operator)
      case operator
      when ADD
        a + b
      when MUL
        a * b
      when CONCAT
        "#{a}#{b}".to_i
      else
        raise ArgumentError
      end
    end
  end
end