require 'set'

module Aoc
  class Day21 < Day
    D_PAD = [
      %w[# ^ A],
      %w[< v >],
    ]

    NUM_PAD = [
      %w[7 8 9],
      %w[4 5 6],
      %w[1 2 3],
      %w[# 0 A],
    ]

    def part1
      # this one was tricky... I couldn't get it working by doing path finding algorithms. I would
      # always end up too low or too high, and sometimes I'd get the tests to pass but not my input.
      # Had to resort to the Reddit threads to get some hints.
      door_unlock_codes.map { |duc| complexity(duc, 2) }.sum
    end

    def part2
      door_unlock_codes.map { |duc| complexity(duc, 25) }.sum
    end

    def complexity(door_unlock_code, total_d_pad_robots)
      num_pad_steps = path(door_unlock_code, num_pad_key_to_position, num_pad_position_to_key)
      result = (0...total_d_pad_robots).reduce(num_pad_steps) {
        |steps, _| path(steps, d_pad_key_to_position, d_pad_position_to_key)
      }

      result.length * digits_of_door_unlock_code(door_unlock_code)
    end

    def digits_of_door_unlock_code(door_unlock_code)
      door_unlock_code.filter { |ch| ch.match(/^\d$/) }.join("").to_i
    end

    def door_unlock_codes
      @door_unlock_codes ||= lines.map { |l| l.strip.chars }
    end

    def path(keys, key_to_position, position_to_key)
      to_press = (['A'] + keys)

      (1...to_press.length)
        .map { |i| steps(to_press[i - 1], to_press[i], key_to_position, position_to_key) }
        .reduce { |a, b| a + b }
    end

    def steps(source, target, key_to_position, position_to_key)
      source_x, source_y = key_to_position[source]
      target_x, target_y = key_to_position[target]
      delta_x = target_x - source_x
      delta_y = target_y - source_y
      horizontal_steps = (delta_x < 0 ? ["<"] : [">"]) * delta_x.abs
      vertical_steps = (delta_y < 0 ? ["^"] : ["v"]) * delta_y.abs

      steps_to_take =
        if delta_x > 0 && position_to_key.member?([source_x, target_y])
          # we're moving right AND the vertical position that we're moving to (from the source) is
          # in the grid
          vertical_steps + horizontal_steps
        elsif position_to_key.member?([target_x, source_y])
          # the horizontal position we're moving to is in the grid
          horizontal_steps + vertical_steps
        else
          # we're moving either left, or only vertically AND moving horizontally first would result
          # in moving out of the grid
          vertical_steps + horizontal_steps
        end

      steps_to_take + ["A"]
    end

    def num_pad_key_to_position
      @num_pad_key_to_position ||=
        (0...NUM_PAD.length).flat_map { |y| (0...NUM_PAD[y].length).map { |x| [NUM_PAD[y][x], [x, y]] } }
                            .reject { |entry| entry[0] == '#' }
                            .to_h
    end

    def num_pad_position_to_key
      @num_pad_position_to_key ||=
        num_pad_key_to_position.entries.map { |entry| [entry[1], entry[0]] }.to_h
    end

    def d_pad_key_to_position
      @d_pad_key_to_position ||=
        (0...D_PAD.length).flat_map { |y| (0...D_PAD[y].length).map { |x| [D_PAD[y][x], [x, y]] } }
                          .reject { |entry| entry[0] == '#' }
                          .to_h
    end

    def d_pad_position_to_key
      @d_pad_position_to_key ||=
        d_pad_key_to_position.entries.map { |entry| [entry[1], entry[0]] }.to_h
    end
  end
end
