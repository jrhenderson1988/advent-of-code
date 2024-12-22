module Aoc
  class Day22 < Day
    def part1
      initial_secret_numbers.map { |sn| nth_secret_number(2000, sn) }.sum
    end

    def part2
      "TODO"
    end

    def nth_secret_number(n, initial_secret_number)
      (0...n).reduce(initial_secret_number) { |prev, _| next_secret_number(prev) }
    end

    def next_secret_number(prev)
      step1 = prune(mix(prev, mul64(prev)))
      step2 = prune(mix(step1, div32(step1)))

      prune(mix(step2, mul2048(step2)))
    end

    def initial_secret_numbers
      @initial_secret_numbers ||= lines.map { |line| line.strip.to_i }
    end

    def mix(secret_number, value)
      secret_number ^ value
    end

    def prune(value)
      value % 16777216
    end

    def mul64(secret_number)
      secret_number * 64
    end

    def mul2048(secret_number)
      secret_number * 2048
    end

    def div32(secret_number)
      secret_number / 32
    end
  end
end