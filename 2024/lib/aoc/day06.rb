require("set")

module Aoc
  class Day06 < Day
    LEFT = '<'
    RIGHT = '>'
    UP = '^'
    DOWN = 'v'
    OBSTACLE = '#'
    OPEN = '.'

    def part1
      states, _ = find_all_guard_states(obstacle_coordinates)
      states.map { |state| state[0] } # we only care about unique positions
            .to_set
            .length
    end

    def part2
      open_coordinates.filter { |point| creates_loop(point) }.length
    end

    def grid
      @grid ||= @content.strip.lines.map { |line| line.strip.chars }
    end

    def obstacle_coordinates
      @obstacle_coordinates ||= all_coordinates.filter { |point| obstacle?(cell_at(point)) }.to_set
    end

    def open_coordinates
      all_coordinates.reject { |point| point == initial_guard_position || obstacle?(cell_at(point)) }
    end

    def all_coordinates
      (0..max_x - 1).flat_map { |x| (0..max_y - 1).map { |y| [x, y] } }
    end

    def max_x
      @max_x ||= grid[0].length
    end

    def max_y
      @max_y ||= grid.length
    end

    def initial_guard_position
      @initial_guard_position ||=
        (0..grid.length - 1).flat_map { |y| (0..grid[y].length - 1).map { |x| [x, y] } }
                            .filter { |point| guard?(cell_at(point)) }.first
    end

    def initial_guard_direction
      @initial_guard_direction ||= cell_at(initial_guard_position)
    end

    # Returns all states (position, direction) the guard has been in after walking either in a loop
    # or out of bounds, as well as true, if the guard ended up in a loop (visited same state as a
    # previous state) or false, if the guard went out of bounds.
    def find_all_guard_states(obstacle_coordinates)
      state = [initial_guard_position, initial_guard_direction]

      visited = [state].to_set
      while true
        state = take_step(state, obstacle_coordinates)
        if state[0] == nil
          return visited, false # return all states when we're out of bounds
        elsif visited.member?(state)
          return visited, true # return all states when there's a loop
        end
        visited.add(state)
      end
    end

    def take_step(state, obstacle_coordinates)
      pos, dir = state
      new_pos = apply_delta(pos, delta(dir))
      if out_of_bounds(new_pos)
        [nil, dir]
      elsif obstacle_coordinates.member?(new_pos)
        # The trick was here, making this recursive. Previously I was just turning right and
        # assuming that the next step was open, but it's entirely possible to hit a corner where
        # there are two obstacles placed that prevent an immediate right turn, and so the guard
        # makes two right turns, resulting in a 180 degree spin anc walking back the way it came.
        take_step([pos, turn_right(dir)], obstacle_coordinates)
      else
        [new_pos, dir]
      end
    end

    def guard?(ch)
      ch == LEFT || ch == RIGHT || ch == UP || ch == DOWN
    end

    def obstacle?(ch)
      ch == OBSTACLE
    end

    def cell_at(point)
      grid[point[1]][point[0]]
    end

    def delta(dir)
      case dir
      when UP
        [0, -1]
      when DOWN
        [0, 1]
      when LEFT
        [-1, 0]
      when RIGHT
        [1, 0]
      else
        raise ArgumentError
      end
    end

    def turn_right(dir)
      case dir
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

    def apply_delta(pos, delta)
      [pos[0] + delta[0], pos[1] + delta[1]]
    end

    def out_of_bounds(point)
      x = point[0]
      y = point[1]
      x < 0 || y < 0 || x >= max_x || y >= max_y
    end

    def creates_loop(point)
      false
      new_obstacle_coordinates = obstacle_coordinates.clone
      new_obstacle_coordinates.add(point)

      _, loop = find_all_guard_states(new_obstacle_coordinates)
      loop
    end
  end
end