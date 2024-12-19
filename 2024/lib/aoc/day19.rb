module Aoc
  class Day19 < Day
    def part1
      desired_designs.filter { |design| design_possible?(design) }.length
    end

    def part2
      "TODO"
    end

    def design_possible?(design, level = 0)
      # puts("#{"." * level}#{design}")
      if design.length == 0
        return true
      end

      any_possible = false
      for pattern in patterns
        if design[0..pattern.length - 1] == pattern
          # puts("=== #{pattern}")
          if design_possible?(design[pattern.length..], level + 1)
            any_possible = true
          end
        end
      end

      any_possible
    end

    def patterns
      chunks[0].strip.split(",").map { |p| p.strip }
    end

    def desired_designs
      chunks[1].strip.lines.map { |l| l.strip }
    end
  end
end