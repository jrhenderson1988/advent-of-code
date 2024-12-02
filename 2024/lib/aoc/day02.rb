module Aoc
  class Day02 < Day
    def part1
      parse.filter { |report| safe?(report) }.length
    end

    def part2
      parse.filter { |report| dampener_safe?(report) }.length
    end

    def parse
      @content.strip.lines.map { |line| line.strip.split(/\s+/).map { |reading| reading.to_i } }
    end

    def safe?(report)
      diffs = report.each_cons(2).map { |a,b| b-a }
      all_positive = diffs.all? { |n| n >= 1 }
      all_negative = diffs.all? { |n| n <= -1 }
      all_safe_jumps = diffs.all? { |n| n.abs >= 1 && n.abs <= 3 }

      (all_positive || all_negative) && all_safe_jumps
    end

    def dampener_safe?(report)
      safe?(report) || (0..report.length).any? { |idx| safe?(remove_bad_level(report, idx)) }
    end

    def remove_bad_level(report, idx)
      (0..report.length-1).filter { |i| i != idx }.map { |i| report[i] }
    end
  end
end