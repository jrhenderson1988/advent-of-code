require 'set'

module Aoc
  # This one was tough and I had to do quite a bit of reading on Reddit as to how to get this to
  # work. I couldn't quite get part 1 working initially due to never quite getting the order of
  # moves right. Part 2 was a bit easier once part 1 was down but was a bit of a rewrite, adding
  # memoization, but it was still challenging. Inspired by some other great solutions.
  class Day21 < Day
    NUM_PAD = "789456123#0A".chars
    D_PAD = "#^A<v>".chars

    def part1
      sum_of_complexities(door_unlock_codes, 2)
    end

    def part2
      sum_of_complexities(door_unlock_codes, 25)
    end

    def sum_of_complexities(door_unlock_codes, total_robots)
      door_unlock_codes.map { |duc| complexity(duc, total_robots) }.sum
    end

    def complexity(door_unlock_code, total_robots)
      digits = digits_of_door_unlock_code(door_unlock_code)
      digits.to_i * length_of_shortest_sequence(digits, total_robots)
    end

    def path(a, b)
      pad = choose_pad(a, b)
      ay, ax = pad.index { |c| c == a }.divmod(3)
      by, bx = pad.index { |c| c == b }.divmod(3)
      s = repeat(">", bx - ax) + repeat("v", by - ay) + repeat("^", ay - by) + repeat("<", ax - bx)
      if pad[ay * 3 + bx] == "#" || pad[by * 3 + ax] == "#"
        s
      else
        s.reverse
      end
    end

    def length_of_shortest_sequence(door_unlock_code, total_robots)
      @complexity_p2_cache ||= {}

      unless @complexity_p2_cache.member?([door_unlock_code, total_robots])
        @complexity_p2_cache[[door_unlock_code, total_robots]] =
          if total_robots < 0
            door_unlock_code.length + 1
          else
            steps(door_unlock_code)
              .map { |a, b| length_of_shortest_sequence(path(a, b), total_robots - 1) }
              .sum
          end
      end

      @complexity_p2_cache[[door_unlock_code, total_robots]]
    end

    def repeat(str, n)
      n >= 0 ? str * n : ""
    end

    def choose_pad(a, b)
      [NUM_PAD, D_PAD].find { |pad| pad.include?(a) && pad.include?(b) }
    end

    # "029" -> [["A", "0"], ["0", "2"], ["2", "9"], ["9", "A"]]
    def steps(door_unlock_code)
      start_a = "A#{door_unlock_code}".chars
      end_a = "#{door_unlock_code}A".chars

      start_a.zip(end_a)
    end

    def digits_of_door_unlock_code(door_unlock_code)
      door_unlock_code.filter { |ch| ch.match(/^\d$/) }.join("")
    end

    def door_unlock_codes
      @door_unlock_codes ||= lines.map { |l| l.strip.chars }
    end
  end
end
