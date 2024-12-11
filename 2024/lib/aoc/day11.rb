module Aoc
  class Day11 < Day
    def part1
      simulate(stones, 25).length
    end

    def part2
      simulate(stones, 75).length
    end

    def simulate(stones, times)
      (0..times-1).reduce(stones) do |s, i|
        puts("#{i}...")
        blink(s, i+1)
      end
    end

    def blink(stones, current)
      new_stones = []

      stones.each do |stone|
        if stone == 0
          new_stones = new_stones + [1]
        elsif stone.to_s.length % 2 == 0
          s = stone.to_s
          mid = (s.length / 2)
          left = s[0..mid-1].to_i
          right = s[mid..].to_i
          new_stones = new_stones + [left, right]
        else
          new_stones = new_stones + [(stone * 2024)]
        end
      end

      puts("#{current}: #{new_stones.length}")
      new_stones
    end

    def stones
      content.strip.split(" ").map { |s| s.strip.to_i }
    end
  end
end