require 'set'

module Aoc
  class Day20 < Day
    def part1
      original_path = run_race_without_cheating

      total_cells = original_path.length + walls.length
      if total_cells != width * height
        # the racetrack is a single path through the walls, there's only a single possible path
        # we could effectively ignore the walls and only consider the racetrack.
        raise ArgumentError, "Invalid racetrack"
      end

      total_cheats_that_save(original_path, distances_to_finish(original_path), 2, p1_threshold)
    end

    def part2
      original_path = run_race_without_cheating

      total_cells = original_path.length + walls.length
      if total_cells != width * height
        raise ArgumentError, "Invalid racetrack"
      end

      total_cheats_that_save(original_path, distances_to_finish(original_path), 20, p2_threshold)
    end

    def possible_cheats_at(point, distance)
      surrounding_points_with_distance(point, distance)
        .reject { |cheat| cheat[0] == point }
        .filter { |cheat| racetrack?(cheat[0]) }
        .to_set
    end

    def surrounding_points_with_distance(point, max_distance)
      x, y = point

      min_y = y - max_distance
      max_y = y + max_distance

      (min_y..max_y).flat_map { |dy|
        distance_from_y = (y - dy).abs
        min_x = x - (max_distance - distance_from_y)
        max_x = x + (max_distance - distance_from_y)

        (min_x..max_x).map { |dx|
          distance_from_x = (x - dx).abs

          p = [dx, dy]
          d = distance_from_y + distance_from_x
          [p, d]
        }
      }
    end

    def p1_threshold
      test? ? 10 : 100
    end

    def p2_threshold
      test? ? 50 : 100
    end

    def run_race_without_cheating
      q = [start]
      explored = [start].to_set

      prev = { start => nil }
      until q.empty?
        v = q.shift

        if v == finish
          node = v
          path = []
          until node.nil?
            path = [node] + path
            node = prev[node]
          end
          return path
        end

        neighbours(v).each { |w|
          unless explored.member?(w)
            explored.add(w)
            prev[w] = v
            q.append(w)
          end
        }
      end

      nil
    end

    def distances_to_finish(original_path)
      (0..original_path.length - 1).map { |i| [original_path[i], original_path.length - i - 1] }
                                   .to_h
    end

    def possible_scores_cheating_at_position(pos, path, dist_to_finish, max_cheat_dist)
      possible_cheats_at(path[pos], max_cheat_dist)
        .filter { |c| racetrack?(c[0]) }
        .map { |c| dist_to_finish[c[0]] + c[1] + pos }
    end

    def total_cheats_that_save(path, dist_to_finish, max_cheat_dist, threshold)
      (0..path.length - 1)
        .flat_map { |i| possible_scores_cheating_at_position(i, path, dist_to_finish, max_cheat_dist) }
        .map { |poss_score| [path.length - 1 - poss_score, 0].max }
        .filter { |poss_score| poss_score >= threshold }
        .length
    end

    def neighbours(point)
      adjacent_points(point).reject { |n| wall?(n) }
    end

    def adjacent_points(point)
      [[1, 0], [0, 1], [-1, 0], [0, -1]]
        .map { |delta| apply_delta(point, delta) }
        .filter { |ap| in_bounds?(ap) }
    end

    def cell_at(point)
      x, y = point
      grid[y][x]
    end

    def apply_delta(point, delta)
      x, y = point
      dx, dy = delta
      [x + dx, y + dy]
    end

    def width
      @width ||= grid[0].length
    end

    def height
      @height ||= grid.length
    end

    def coordinates
      @coordinates ||= (0..height - 1).flat_map { |y| (0..width - 1).map { |x| [x, y] } }
    end

    def wall?(point)
      walls.member?(point)
    end

    def racetrack?(point)
      in_bounds?(point) && !wall?(point)
    end

    def in_bounds?(point)
      x, y = point
      x >= 0 && x < width && y >= 0 && y < height
    end

    def walls
      @walls ||= coordinates.filter { |point| cell_at(point) == '#' }.to_set
    end

    def start
      @start ||= coordinates.filter { |point| cell_at(point) == 'S' }.first
    end

    def finish
      @finish ||= coordinates.filter { |point| cell_at(point) == 'E' }.first
    end

    def grid
      @grid ||= lines.map { |line| line.strip.chars }
    end
  end
end