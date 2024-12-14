module Aoc
  class Day14 < Day
    def part1
      grid_width = test? ? 11 : 101
      grid_height = test? ? 7 : 103

      calculate_safety_factor(
        simulate_moves(robots, 100, grid_width, grid_height),
        grid_width,
        grid_height
      )
    end

    def part2
      "TODO"
    end

    def simulate_moves(robots, moves, grid_width, grid_height)
      robots.map { |robot| move_robot(robot, moves, grid_width, grid_height) }
    end

    def move_robot(robot, times, grid_width, grid_height)
      position, velocity = robot
      px, py = position
      vx, vy = velocity
      new_position = [px + (vx * times), py + (vy * times)]

      new_position_in_grid = [new_position[0] % grid_width, new_position[1] % grid_height]
      [new_position_in_grid, velocity]
    end

    def robots
      @robots ||= lines.map { |line| parse_line(line) }
    end

    def parse_line(line)
      px, py, vx, vy = line.match(/p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)/).captures
      [[px.to_i, py.to_i], [vx.to_i, vy.to_i]]
    end

    def calculate_safety_factor(robots, grid_width, grid_height)
      quadrant_width = grid_width / 2
      quadrant_height = grid_height / 2

      top_left = [[0, 0], [quadrant_width - 1, quadrant_height - 1]]
      top_right = [[grid_width - quadrant_width, 0], [grid_width - 1, quadrant_height - 1]]
      bottom_left = [[0, grid_height - quadrant_height], [quadrant_width - 1, grid_height - 1]]
      bottom_right = [[quadrant_width + 1, quadrant_height + 1], [grid_width - 1, grid_height - 1]]

      total_top_left = total_robots_in_quadrant(robots, top_left)
      total_top_right = total_robots_in_quadrant(robots, top_right)
      total_bottom_left = total_robots_in_quadrant(robots, bottom_left)
      total_bottom_right = total_robots_in_quadrant(robots, bottom_right)

      total_top_left * total_top_right * total_bottom_left * total_bottom_right
    end

    def total_robots_in_quadrant(robots, quadrant)
      top_left, bottom_right = quadrant
      min_x, min_y = top_left
      max_x, max_y = bottom_right

      robots_in_quadrant = robots.filter do |robot|
        position, _ = robot
        x, y = position
        x >= min_x && x <= max_x && y >= min_y && y <= max_y
      end

      robots_in_quadrant.length
    end
  end
end