require 'set'

module Aoc
  class Day18 < Day
    DIRECTIONS = [[1, 0], [0, 1], [-1, 0], [0, -1]]

    def part1
      fallen_byte_positions =
        (0..total_bytes_to_fall - 1).map { |i| falling_byte_positions[i] }.to_set
      fewest_steps_to_end([0, 0], [grid_max, grid_max], fallen_byte_positions)
    end

    def part2
      (0..falling_byte_positions.length - 1).each do |i|
        fallen_byte_positions = (0..i - 1).map { |j| falling_byte_positions[j] }.to_set
        result = fewest_steps_to_end([0, 0], [grid_max, grid_max], fallen_byte_positions)
        if result.nil?
          return falling_byte_positions[i - 1].join(",")
        end
      end

      nil
    end

    def fewest_steps_to_end(start, finish, fallen_byte_positions)
      queue = [start]
      explored = Set[start]
      prev = { start => nil }

      until queue.empty?
        v = queue.shift
        if v == finish
          node = v
          steps = Set[]
          until node.nil?
            steps.add(node)
            node = prev[node]
          end

          return steps.length - 1
        end

        neighbours(v, fallen_byte_positions).each { |w|
          unless explored.member?(w)
            explored.add(w)
            prev[w] = v
            queue.append(w)
          end
        }
      end

      nil
    end

    def neighbours(point, fallen_byte_positions)
      DIRECTIONS.map { |delta| apply_delta(point, delta) }
                .filter { |p| in_bounds?(p) }
                .reject { |p| fallen_byte_positions.member?(p) }
    end

    def falling_byte_positions
      @falling_byte_positions ||= lines.map { |line| line.split(",").map { |n| n.to_i } }
    end

    def grid_max
      test? ? 6 : 70
    end

    def total_bytes_to_fall
      test? ? 12 : 1024
    end

    def in_bounds?(point)
      x, y = point
      x >= 0 && x <= grid_max && y >= 0 && y <= grid_max
    end

    def apply_delta(point, delta)
      x, y = point
      dx, dy = delta
      [x + dx, y + dy]
    end
  end
end
