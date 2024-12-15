require 'set'

module Aoc
  class Day15 < Day
    WALL = '#'
    BOX = 'O'
    ROBOT = '@'
    EMPTY = '.'
    UP = '^'
    RIGHT = '>'
    DOWN = 'v'
    LEFT = '<'

    def part1
      position, boxes = [initial_robot_position, initial_boxes]
      _, boxes = simulate_robot(instructions, position, boxes)
      sum_of_box_gps_coordinates(boxes)
    end

    def part2
      "TODO"
    end

    def simulate_robot(instructions, position, boxes)
      instructions.each do |instruction|
        position, boxes = execute_instruction(instruction, position, boxes)
      end

      [position, boxes]
    end

    def execute_instruction(instruction, position, boxes)
      delta = to_delta(instruction)
      new_position = apply_delta(position, delta)
      should_move = true
      boxes_to_move = Set[]
      while true
        if wall?(new_position)
          should_move = false
          break
        elsif box?(boxes, new_position)
          boxes_to_move.add(new_position)
          new_position = apply_delta(new_position, delta)
        elsif empty?(boxes, new_position)
          should_move = true
          break
        else
          raise ArgumentError
        end
      end

      if !should_move
        [position, boxes]
      else
        stationary_boxes = boxes.subtract(boxes_to_move)
        moved_boxes = boxes_to_move.map { |box_pos| apply_delta(box_pos, delta) }
        boxes = stationary_boxes.union(moved_boxes)

        new_position = apply_delta(position, delta)
        [new_position, boxes]
      end

    end

    def sum_of_box_gps_coordinates(boxes)
      boxes.map { |pos| to_gps_coordinate(pos) }.sum
    end

    def to_gps_coordinate(point)
      x, y = point
      (y * 100) + x
    end

    def instructions
      @instructions ||= chunks[1].strip.lines.map { |line| line.strip }.join("").chars
    end

    def grid
      @grid ||= chunks[0].strip.lines.map { |line| line.strip.chars }
    end

    def walls
      @walls ||= coordinates.filter { |coord| grid[coord[1]][coord[0]] == WALL }.to_set
    end

    def initial_boxes
      @initial_boxes ||= coordinates.filter { |coord| grid[coord[1]][coord[0]] == BOX }.to_set
    end

    def wall?(point)
      walls.member?(point)
    end

    def box?(box_positions, point)
      box_positions.member?(point)
    end

    def empty?(box_positions, point)
      !wall?(point) && !box?(box_positions, point)
    end

    def coordinates
      @coordinates ||= (0..grid_height - 1).flat_map { |y| (0..grid_width - 1).map { |x| [x, y] } }
    end

    def grid_width
      @grid_width ||= grid[0].length
    end

    def grid_height
      @grid_height ||= grid.length
    end

    def initial_robot_position
      @initial_robot_position ||=
        (0..grid_height - 1).flat_map { |y| (0..grid_width - 1).map { |x| [x, y] } }
                            .filter { |point| grid[point[1]][point[0]] == ROBOT }
                            .first
    end

    def chunks
      @chunks ||= content.gsub(/\r\n|\r|\n/, "\n").split("\n\n")
    end

    def to_delta(direction)
      case direction
      when UP
        [0, -1]
      when RIGHT
        [1, 0]
      when DOWN
        [0, 1]
      when LEFT
        [-1, 0]
      else
        nil
      end
    end

    def apply_delta(position, delta)
      x, y = position
      dx, dy = delta
      [x + dx, y + dy]
    end
  end
end