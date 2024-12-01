module Aoc
  class Day01 < Day
    def part1
      lists = @content.strip
                      .lines
                      .map { |line| line.strip.split(/\s+/).map { |n| n.to_i } }
                      .reduce([[], []]) { |vals, n| [vals[0] + [n[0]], vals[1] + [n[1]]] }
      lists[0].sort.zip(lists[1].sort).map { |nums| (nums[0] - nums[1]).abs }.sum
    end

    def part2
      lists = @content.strip
                      .lines
                      .map { |line| line.strip.split(/\s+/).map { |n| n.to_i } }
                      .reduce([[], []]) { |vals, n| [vals[0] + [n[0]], vals[1] + [n[1]]] }
      lists[0].map { |n| n * occurrences_of(lists[1], n) }.sum
    end

    def occurrences_of(list, n)
      list.filter { |v| v == n }.length
    end
  end
end