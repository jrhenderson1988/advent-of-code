module Aoc
  class Day03 < Day
    def part1
      @content.scan(/(mul\((\d{1,3}),(\d{1,3})\))/)
              .map { |match| match[1].to_i * match[2].to_i }
              .sum
    end

    def part2
      result = @content.scan(/(don't\(\)|do\(\)|mul\((\d{1,3}),(\d{1,3})\))/)
                       .reduce([0, true]) { |acc, match| apply_conditional(acc, match) }
      result[0]
    end

    def apply_conditional(acc, match)
      if match[0] == "don't()"
        [acc[0], false]
      elsif match[0] == 'do()'
        [acc[0], true]
      elsif match[0].start_with?("mul") && acc[1] == true
        [acc[0] + (match[1].to_i * match[2].to_i), true]
      else
        acc
      end
    end
  end
end