module Aoc
  class Day19 < Day
    def part1
      desired_designs.filter { |design| possible_arrangements(design) > 0 }.length
    end

    def part2
      desired_designs.map { |design| possible_arrangements(design) }.sum
    end

    def possible_arrangements(design, cache = {})
      if design.length == 0
        return 1
      end

      if cache[design].nil?
        cache[design] =
          patterns.filter { |pattern| design[0..pattern.length - 1] == pattern }
                  .map { |pattern| possible_arrangements(design[pattern.length..], cache) }
                  .sum
      else
        cache[design]
      end
    end

    def patterns
      chunks[0].strip.split(",").map { |p| p.strip }
    end

    def desired_designs
      chunks[1].strip.lines.map { |l| l.strip }
    end
  end
end