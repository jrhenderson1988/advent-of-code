require 'set'

module Aoc
  class Day16 < Day
    UP = [0, -1]
    RIGHT = [1, 0]
    DOWN = [0, 1]
    LEFT = [-1, 0]
    DIRECTIONS = [UP, RIGHT, DOWN, LEFT]

    def part1
      find_cheapest_path_cost(start_point, end_point, [1, 0])
    end

    def part2
      "TODO"
    end

    def width
      grid[0].length
    end

    def height
      grid.length
    end

    def walls
      @walls ||= coordinates.filter { |point| grid[point[1]][point[0]] == '#' }.to_set
    end

    def start_point
      @start_point ||= coordinates.filter { |point| grid[point[1]][point[0]] == 'S' }.first
    end

    def end_point
      @end_point ||= coordinates.filter { |point| grid[point[1]][point[0]] == 'E' }.first
    end

    def coordinates
      @coordinates ||= (0..height - 1).flat_map { |y| (0..width - 1).map { |x| [x, y] } }
    end

    def grid
      @grid ||= lines.map { |line| line.strip.chars }
    end

    def find_cheapest_path_cost(start, target, initial_direction = [1, 0])
      source = [start, initial_direction]
      visited = Set[source]
      dist = { source => 0 }
      prev = { source => nil }
      queue = [source]

      until queue.empty?
        u_idx = (0..queue.length - 1).reduce { |a, b| dist[queue[a]] < dist[queue[b]] ? a : b }
        u = queue[u_idx]
        queue.delete_at(u_idx)

        for neighbour in neighbours(u)
          v, cost = neighbour
          unless visited.member?(v)
            visited.add(v)
            queue.append(v)
            alt = dist[u] + cost
            if dist[v].nil? || alt < dist[v]
              dist[v] = alt
              prev[v] = u
            end
          end
        end
      end

      dist.filter { |state, _| state[0] == target }
          .map { |_, distance| distance }
          .min
    end

    def neighbours(state)
      DIRECTIONS.map { |delta| neighbour(state, delta) }
                .filter do |neighbour|
        empty_space?(neighbour)
      end
    end

    def neighbour(state, delta)
      point, facing = state
      new_point = apply_delta(point, delta)
      # 1 - for moving same direction
      # 1001 - 1000 for the turn, and then 1 for moving in that new direction
      cost = delta == facing ? 1 : 1001

      [[new_point, delta], cost]
    end

    def empty_space?(neighbour)
      state, _ = neighbour
      point, _ = state

      !walls.member?(point)
    end

    def apply_delta(point, delta)
      x, y = point
      dx, dy = delta
      [x + dx, y + dy]
    end
  end
end