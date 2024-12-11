module Aoc
  class Day11 < Day
    def part1
      total_stones(blink_n_times(stones, 25))
    end

    def part2
      total_stones(blink_n_times(stones, 75))
    end

    def blink_n_times(stones, times)
      stone_counts = {}

      stones.each do |stone|
        count = stone_counts[stone] || 0
        stone_counts[stone] = count + 1
      end

      (0..times - 1).reduce(stone_counts) { |sc| blink(sc) }
    end

    def blink(stones_counts)
      new_stone_counts = {}

      stones_counts.each do |stone, count|
        if stone == 0
          new_stone_counts[1] = (new_stone_counts[1] || 0) + count
        elsif stone.to_s.length % 2 == 0
          s = stone.to_s
          mid = (s.length / 2)
          left = s[0..mid - 1].to_i
          right = s[mid..].to_i

          new_stone_counts[left] = (new_stone_counts[left] || 0) + count
          new_stone_counts[right] = (new_stone_counts[right] || 0) + count
        else
          new_stone_counts[2024 * stone] = (new_stone_counts[2024 * stone] || 0) + count
        end
      end

      new_stone_counts
    end

    def total_stones(stone_counts)
      stone_counts.values.sum
    end

    def stones
      content.strip.split(" ").map { |s| s.strip.to_i }
    end
  end
end