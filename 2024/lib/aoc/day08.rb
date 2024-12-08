require 'set'

module Aoc
  class Day08 < Day
    def part1
      find_unique_antinode_locations(antenna_locations).length
    end

    def part2
      find_resonant_antinode_locations(antenna_locations).length
    end

    def grid
      @grid ||= lines.map { |line| line.strip.chars }
    end

    def width
      @width ||= grid[0].length
    end

    def height
      @height ||= grid.length
    end

    def antenna_locations
      coordinates.filter { |coord| is_antenna(char_at(coord)) }
                 .reduce({}) do |acc, coord|
        ch = char_at(coord)
        acc[ch] = (acc[ch] || []) + [coord]
        acc
      end
    end

    def coordinates
      (0..height - 1).flat_map { |y| (0..width - 1).map { |x| [x, y] } }
    end

    def char_at(coord)
      x = coord[0]
      y = coord[1]
      grid[y][x]
    end

    def is_antenna(ch)
      ch != '.'
    end

    def find_unique_antinode_locations(antenna_locations)
      unique_locations = Set[]

      antenna_locations.each do |_, locations|
        pairs(locations).map { |pair| find_both_antinode_positions_for_pair(pair) }
                        .each { |positions| positions.each { |pos| unique_locations.add(pos) } }
      end

      unique_locations
    end

    def pairs(locations)
      (0..locations.length - 1).flat_map do |first|
        (first + 1..locations.length - 1).map { |second| [locations[first], locations[second]] }
      end
    end

    def find_both_antinode_positions_for_pair(pair)
      first, second = pair
      diff_x = second[0] - first[0]
      diff_y = second[1] - first[1]

      positions = []

      first_antinode = [first[0] - diff_x, first[1] - diff_y]
      if in_bounds?(first_antinode)
        positions = positions + [first_antinode]
      end

      second_antinode = [second[0] + diff_x, second[1] + diff_y]
      if in_bounds?(second_antinode)
        positions = positions + [second_antinode]
      end

      positions
    end

    def find_resonant_antinode_locations(antenna_locations)
      unique_locations = Set[]

      antenna_locations.each do |_, locations|
        pairs(locations).map { |pair| find_resonant_antinode_positions_for_pair(pair) }
                        .each { |positions| positions.each { |pos| unique_locations.add(pos) } }

      end

      unique_locations
    end

    def find_resonant_antinode_positions_for_pair(pair)
      first, second = pair
      diff_x = second[0] - first[0]
      diff_y = second[1] - first[1]

      positions = [first,second]

      curr = first
      while true
        next_antinode = [curr[0] - diff_x, curr[1] - diff_y]
        if in_bounds?(next_antinode)
          positions = positions + [next_antinode]
          curr = next_antinode
        else
          break
        end
      end

      curr = second
      while true
        next_antinode = [curr[0] + diff_x, curr[1] + diff_y]
        if in_bounds?(next_antinode)
          positions = positions + [next_antinode]
          curr = next_antinode
        else
          break
        end
      end

      positions
    end

    def in_bounds?(coord)
      coord[0] >= 0 && coord[0] < width && coord[1] >= 0 && coord[1] < height
    end
  end
end