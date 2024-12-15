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
      g = grid
      position, boxes, walls = [initial_robot_position(g), initial_boxes(g), walls(g)]
      _, boxes = simulate_robot(instructions, position, walls, boxes)
      sum_of_box_gps_coordinates(boxes)
    end

    def part2
      "TODO"
    end

    def simulate_robot(instructions, position, walls, boxes)
      instructions.each do |instruction|
        position, boxes = execute_instruction(instruction, position, walls, boxes)
      end

      [position, boxes]
    end

    def execute_instruction(instruction, position, walls, boxes)
      delta = to_delta(instruction)
      new_position = apply_delta(position, delta)
      should_move = true
      boxes_to_move = Set[]
      while true
        if wall?(walls, new_position)
          should_move = false
          break
        elsif box?(boxes, new_position)
          boxes_to_move.add(new_position)
          new_position = apply_delta(new_position, delta)
        elsif empty?(walls, boxes, new_position)
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

    def walls(grid)
      coords(grid).filter { |coord| grid[coord[1]][coord[0]] == WALL }.to_set
    end

    def initial_boxes(grid)
      coords(grid).filter { |coord| grid[coord[1]][coord[0]] == BOX }.to_set
    end

    def wall?(wall_positions, point)
      wall_positions.member?(point)
    end

    def box?(box_positions, point)
      box_positions.member?(point)
    end

    def empty?(wall_positions, box_positions, point)
      !wall?(wall_positions, point) && !box?(box_positions, point)
    end

    def coords(grid)
      (0..height(grid) - 1).flat_map { |y| (0..width(grid) - 1).map { |x| [x, y] } }
    end

    def width(grid)
      grid[0].length
    end

    def height(grid)
      grid.length
    end

    def initial_robot_position(grid)
      (0..height(grid) - 1).flat_map { |y| (0..width(grid) - 1).map { |x| [x, y] } }
                           .filter { |point| grid[point[1]][point[0]] == ROBOT }
                           .first
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

    def instructions
      @instructions ||= chunks[1].strip.lines.map { |line| line.strip }.join("").chars
    end

    def grid
      @grid ||= chunks[0].strip.lines.map { |line| line.strip.chars }
    end

    def chunks
      @chunks ||= content.gsub(/\r\n|\r|\n/, "\n").split("\n\n")
    end
  end
end