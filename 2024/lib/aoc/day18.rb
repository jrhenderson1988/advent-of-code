require 'set'

module Aoc
  class Day18 < Day
    DIRECTIONS = [[1, 0], [0, 1], [-1, 0], [0, -1]]

    def part1
      fallen_byte_positions =
        (0..total_bytes_to_fall - 1).map { |i| falling_byte_positions[i] }.to_set
      # print_grid(fallen_byte_positions)
      fewest_steps_to_end([0, 0], [grid_size_inclusive, grid_size_inclusive], fallen_byte_positions)
    end

    def part2
      "TODO"
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
      lines.map { |line| line.split(",").map { |n| n.to_i } }
    end

    def coordinates
      (0..grid_size_inclusive).flat_map { |y| (0..grid_size_inclusive).map { |x| [x, y] } }
    end

    def grid_size_inclusive
      test? ? 6 : 70
    end

    def total_bytes_to_fall
      test? ? 12 : 1024
    end

    def in_bounds?(point)
      x, y = point
      x >= 0 && x <= grid_size_inclusive && y >= 0 && y <= grid_size_inclusive
    end

    def apply_delta(point, delta)
      x, y = point
      dx, dy = delta
      [x + dx, y + dy]
    end

    def print_grid(fallen_byte_positions)
      (0..grid_size_inclusive).each do |y|
        puts((0..grid_size_inclusive).map { |x|
          fallen_byte_positions.member?([x, y]) ? "#" : "."
        }.join(""))
      end
    end
  end
end
