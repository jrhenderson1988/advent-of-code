require 'set'

module Aoc
  class Day12 < Day
    def part1
      regions.map { |region| cost_of_fencing(region) }.sum
    end

    def part2
      regions.map { |region| cost_of_fencing_with_bulk_discount(region) }.sum
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

    def coordinates
      @coordinates ||= (0..height - 1).flat_map { |y| (0..width - 1).map { |x| [x, y] } }
    end

    def plant_at(point)
      x, y = point
      grid[y][x]
    end

    def in_bounds?(point)
      x, y = point
      x >= 0 && x < width && y >= 0 && y < height
    end

    def neighbours(point)
      [[1, 0], [-1, 0], [0, 1], [0, -1]]
        .map { |delta| apply_delta(point, delta) }
        .filter { |p| in_bounds?(p) }
    end

    def neighbours_in_same_plot(point)
      plant = plant_at(point)
      neighbours(point).filter { |p| plant_at(p) == plant }
    end

    def apply_delta(point, delta)
      x, y = point
      dx, dy = delta
      [x + dx, y + dy]
    end

    def regions
      visited = Set[]
      regions = []

      coordinates.each do |point|
        unless visited.member?(point)
          region = discover_region(point)
          regions = regions + [region]
          visited = visited.union(region) # add all region points to visited
        end
      end

      regions
    end

    def discover_region(point, region = Set[])
      if region.member?(point)
        region
      else
        region.add(point)
        neighbours_in_same_plot(point).each do |neighbour|
          region = region.union(discover_region(neighbour, region))
        end
        region
      end
    end

    def cost_of_fencing(region)
      area(region) * perimeter(region)
    end

    def cost_of_fencing_with_bulk_discount(region)
      area(region) * number_of_sides(region)
    end

    def number_of_sides(region)
      region.map { |point| count_corners(point, region) }.sum
    end

    def count_corners(point, region)
      above = apply_delta(point, [0, -1])
      left = apply_delta(point, [-1, 0])
      right = apply_delta(point, [1, 0])
      below = apply_delta(point, [0, 1])

      is_top_left_outer_corner = !region.member?(above) && !region.member?(left)
      is_top_right_outer_corner = !region.member?(above) && !region.member?(right)
      is_bottom_left_outer_corner = !region.member?(below) && !region.member?(left)
      is_bottom_right_outer_corner = !region.member?(below) && !region.member?(right)

      below_right = apply_delta(point, [1, 1])
      below_left = apply_delta(point, [-1, 1])
      above_left = apply_delta(point, [-1, -1])
      above_right = apply_delta(point, [1, -1])

      # perspective looking from the outside of the region in
      is_top_left_inner_corner = region.member?(right) && region.member?(below) && !region.member?(below_right)
      is_top_right_inner_corner = region.member?(left) && region.member?(below) && !region.member?(below_left)
      is_bottom_left_inner_corner = region.member?(right) && region.member?(above) && !region.member?(above_right)
      is_bottom_right_inner_corner = region.member?(left) && region.member?(above) && !region.member?(above_left)

      [
        is_top_left_outer_corner,
        is_top_right_outer_corner,
        is_bottom_left_outer_corner,
        is_bottom_right_outer_corner,
        is_top_left_inner_corner,
        is_top_right_inner_corner,
        is_bottom_left_inner_corner,
        is_bottom_right_inner_corner
      ]
        .filter { |v| v }
        .length
    end

    def perimeter(region)
      region
        .map { |point|
          4 - neighbours(point)
                .filter { |neighbour| region.member?(neighbour) }
                .length
        }
        .sum
    end

    def area(region)
      region.length
    end
  end
end