require 'set'

module Aoc
  class Day14 < Day
    def part1
      calculate_safety_factor(simulate_moves(robots, 100))
    end

    def part2
      find_steps_with_no_overlaps(robots)
    end

    def simulate_moves(robots, moves)
      robots.map { |robot| move_robot(robot, moves) }
    end

    def move_robot(robot, times)
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

    def grid_width
      test? ? 11 : 101
    end

    def grid_height
      test? ? 7 : 103
    end

    def parse_line(line)
      px, py, vx, vy = line.match(/p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)/).captures
      [[px.to_i, py.to_i], [vx.to_i, vy.to_i]]
    end

    def calculate_safety_factor(robots)
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

    def find_steps_with_no_overlaps(robots)
      total_robots = robots.length
      steps = 0
      while true
        steps += 1
        robots = simulate_moves(robots, 1)
        if unique_robot_positions(robots) == total_robots
          return steps
        end
      end
    end

    def unique_robot_positions(robots)
      robots.map { |robot| robot[0] }.to_set.length
    end
  end
end