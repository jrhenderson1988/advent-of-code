require 'set'

module Aoc
  class Day15 < Day

    def part1
      g = grid
      position, boxes, walls = [initial_robot_position(g), initial_boxes(g), walls(g)]
      _, boxes = simulate_robot(instructions, position, walls, boxes)
      sum_of_box_gps_coordinates(boxes)
    end

    def part2
      g = widen(grid)
      position, boxes, walls = [initial_robot_position(g), initial_wide_boxes(g), walls(g)]
      _, boxes = simulate_robot_wide(instructions, position, walls, boxes)
      sum_of_box_gps_coordinates(boxes.map { |box| box[0] })
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
        else # empty
          should_move = true
          break
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

    def simulate_robot_wide(instructions, position, walls, boxes)
      instructions.each do |instruction|
        position, boxes = execute_instruction_wide(instruction, position, walls, boxes)
      end

      [position, boxes]
    end

    def execute_instruction_wide(instruction, position, walls, boxes)
      delta = to_delta(instruction)
      new_position = apply_delta(position, delta)

      if wall?(walls, new_position)
        [position, boxes]
      else
        adjacent_box = wide_box_at(boxes, new_position)
        if !adjacent_box.nil?
          affected_boxes = find_affected_wide_boxes(boxes, adjacent_box, delta)
          if wide_boxes_movable?(walls, affected_boxes, delta)
            updated_boxes = move_affected_wide_boxes(boxes, affected_boxes, delta)
            [new_position, updated_boxes]
          else
            [position, boxes] # can't move boxes, so we cancel the move
          end
        else
          [new_position, boxes]
        end
      end
    end

    def find_affected_wide_boxes(wide_box_positions, starting_box, delta)
      affected = [starting_box].to_set

      for coord in starting_box
        adjacent_box = wide_box_at(wide_box_positions, apply_delta(coord, delta))
        if !adjacent_box.nil? && adjacent_box != starting_box
          affected = affected.union(find_affected_wide_boxes(wide_box_positions, adjacent_box, delta))
        end
      end

      affected
    end

    def wide_boxes_movable?(wall_positions, affected_boxes, delta)
      !affected_boxes.any? { |box| box.any? { |coord| wall?(wall_positions, apply_delta(coord, delta)) } }
    end

    def move_affected_wide_boxes(boxes, affected_boxes, delta)
      boxes_without_affect = boxes.subtract(affected_boxes)
      moved = affected_boxes.map { |box| box.map { |coord| apply_delta(coord, delta) } }
      boxes_without_affect.union(moved)
    end

    def sum_of_box_gps_coordinates(boxes)
      boxes.map { |pos| to_gps_coordinate(pos) }.sum
    end

    def to_gps_coordinate(point)
      x, y = point
      (y * 100) + x
    end

    def walls(grid)
      coords(grid).filter { |coord| grid[coord[1]][coord[0]] == '#' }.to_set
    end

    def initial_boxes(grid)
      coords(grid).filter { |coord| grid[coord[1]][coord[0]] == 'O' }.to_set
    end

    def initial_wide_boxes(grid)
      coords(grid).filter { |coord| grid[coord[1]][coord[0]] == '[' && grid[coord[1]][coord[0] + 1] == ']' }
                  .map { |coord| [coord, [coord[0] + 1, coord[1]]] }
                  .to_set
    end

    def wall?(wall_positions, point)
      wall_positions.member?(point)
    end

    def box?(box_positions, point)
      box_positions.member?(point)
    end

    def wide_box_at(wide_box_positions, point)
      wide_box_positions.filter { |wbp| wbp[0] == point || wbp[1] == point }.first
    end

    def wide_box?(wide_box_positions, point)
      wide_box_positions.filter { |wbp| wbp[0] == point || wbp[1] == point }.length > 0
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
                           .filter { |point| grid[point[1]][point[0]] == '@' }
                           .first
    end

    def to_delta(direction)
      case direction
      when '^'
        [0, -1]
      when '>'
        [1, 0]
      when 'v'
        [0, 1]
      when '<'
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

    def widen(grid)
      grid.map { |line| line.map { |ch| widen_char(ch) }.flatten }
    end

    def widen_char(ch)
      case ch
      when "#"
        %w[# #]
      when "O"
        %w([ ])
      when "."
        %w[. .]
      when "@"
        %w[@ .]
      else
        raise ArgumentError
      end
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