require 'set'

module Aoc
  class Day10 < Day
    def part1
      starting_points.map { |starting_point| get_trailhead_score(starting_point) }.sum
    end

    def part2
      "TODO"
    end

    def grid
      @grid ||= lines.map { |line| line.chars.map { |c| c.to_i } }
    end

    def width
      @width ||= grid[0].length
    end

    def height
      @height ||= grid.length
    end

    def coordinates
      @coordinates ||=
        (0..grid.length - 1).flat_map { |y| (0..grid[y].length - 1).map { |x| [x, y] } }
    end

    def value_at(coord)
      x, y = coord
      grid[y][x]
    end

    def starting_points
      @starting_points ||= coordinates.filter { |coord| value_at(coord) == 0 }
    end

    def neighbours_of(coord)
      x, y = coord
      [[x - 1, y], [x, y - 1], [x + 1, y], [x, y + 1]]
    end

    def valid_neighbours_of(coord)
      target_value = value_at(coord) + 1
      neighbours_of(coord).filter { |n| in_bounds?(n) && value_at(n) == target_value }
    end

    def in_bounds?(coord)
      x, y = coord
      x >= 0 && x < width && y >= 0 && y < height
    end

    def get_trailhead_score(start)
      search_for_reachable_peaks(start).length
    end

    def search_for_reachable_peaks(point, path = [], reachable_peaks = Set[])
      path = path + [point]
      if value_at(point) == 9
        reachable_peaks.add(point)
        reachable_peaks
      else
        for neighbour in valid_neighbours_of(point)
          foo = search_for_reachable_peaks(neighbour, path, reachable_peaks)
          reachable_peaks = reachable_peaks.union(foo)
        end

        reachable_peaks
      end
    end

    def find_all_paths_to_peaks(v, discovered = Set[], path = [])
      discovered.add(v)
      path = path + [v]

      if value_at(v) == 9
        return path
      end

      for w in valid_neighbours_of(v)
        if !discovered.member?(w)
          find_all_paths_to_peaks(w, discovered, path)
        end
      end
    end
  end
end