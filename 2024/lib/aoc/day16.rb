require 'set'

module Aoc
  class Day16 < Day
    UP = [0, -1]
    RIGHT = [1, 0]
    DOWN = [0, 1]
    LEFT = [-1, 0]
    DIRECTIONS = [UP, RIGHT, DOWN, LEFT]

    def part1
      find_best_path_score
    end

    def part2
      best_score = find_best_path_score
      total_cells_in_best_paths(best_score)
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

    def find_best_path_score
      source = [start_point, RIGHT]
      visited = Set[source]
      dist = { source => 0 }
      prev = { source => nil }
      queue = [source]

      until queue.empty?
        u_idx = (0..queue.length - 1).reduce { |a, b| dist[queue[a]] < dist[queue[b]] ? a : b }
        u = queue[u_idx]
        queue.delete_at(u_idx)

        visited.add(u)

        neighbours(u).each do |neighbour|
          v, cost = neighbour

          unless visited.member?(v)
            queue.append(v)
            alt = dist[u] + cost
            if dist[v].nil? || (alt < dist[v])
              dist[v] = alt
              prev[v] = u
            end
          end
        end
      end

      dist.filter { |state, _| state[0] == end_point }
          .map { |_, distance| distance }
          .min
    end

    def total_cells_in_best_paths(best_score)
      queue = [[start_point, RIGHT, 0, [start_point]]]
      visited = {}
      best_paths = []

      until queue.empty?
        pos, dir, score, path = queue.shift
        key = [pos, dir]
        if score > best_score
          next
        elsif !visited[key].nil? && visited[key] < score
          next
        end

        visited[key] = score

        if pos == end_point && score == best_score
          best_paths.append(path)
          next
        end

        new_pos = [pos[0] + dir[0], pos[1] + dir[1]]
        unless walls.member?(new_pos)
          queue.append([new_pos, dir, score + 1, path + [new_pos]])
        end

        queue.append([pos, turn_anti_clockwise(dir), score + 1000, path])
        queue.append([pos, turn_clockwise(dir), score + 1000, path])
      end

      best_paths.flat_map { |best_path| best_path.map { |point| point } }.to_set.length
    end

    def neighbours(state)
      DIRECTIONS.map { |delta| neighbour(state, delta) }
                .filter { |neighbour| empty_space?(neighbour) }
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

    def turn_anti_clockwise(direction)
      case direction
      when UP
        LEFT
      when LEFT
        DOWN
      when DOWN
        RIGHT
      when RIGHT
        UP
      else
        raise ArgumentError
      end
    end

    def turn_clockwise(direction)
      case direction
      when UP
        RIGHT
      when RIGHT
        DOWN
      when DOWN
        LEFT
      when LEFT
        UP
      else
        raise ArgumentError
      end
    end

  end
end