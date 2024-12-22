require 'set'

module Aoc
  class Day21 < Day
    LEFT = "<"
    RIGHT = ">"
    UP = "^"
    DOWN = "v"
    PRESS = "A"
    DIRECTIONS = { UP => [0, -1], RIGHT => [1, 0], DOWN => [0, 1], LEFT => [-1, 0] }

    # +---+---+---+
    # | 7 | 8 | 9 |
    # +---+---+---+
    # | 4 | 5 | 6 |
    # +---+---+---+
    # | 1 | 2 | 3 |
    # +---+---+---+
    #     | 0 | A |
    #     +---+---+
    # NUM_PAD_NEIGHBOURS = {
    #   "A" => { "0" => "<", "3" => "^" },
    #   "0" => { "A" => ">", "2" => "^" },
    #   "1" => { "2" => ">", "4" => "^" },
    #   "2" => { "0" => "v", "1" => "<", "5" => "^", "3" => ">" },
    #   "3" => { "A" => "v", "2" => "<", "6" => "^" },
    #   "4" => { "1" => "v", "7" => "^", "5" => ">" },
    #   "5" => { "2" => "v", "4" => "<", "6" => ">", "8" => "^" },
    #   "6" => { "3" => "v", "5" => "<", "9" => "^" },
    #   "7" => { "4" => "v", "8" => ">" },
    #   "8" => { "7" => "<", "5" => "v", "9" => ">" },
    #   "9" => { "8" => "<", "6" => "v" },
    # }

    #     +---+---+
    #     | ^ | A |
    # +---+---+---+
    # | < | v | > |
    # +---+---+---+
    # D_PAD_NEIGHBOURS = {
    #   "A" => { "^" => "<", ">" => "v" },
    #   "^" => { "A" => ">", "v" => "v" },
    #   "v" => { "^" => "^", "<" => "<", ">" => ">" },
    #   "<" => { "v" => ">" },
    #   ">" => { "v" => "<", "A" => "^" },
    # }

    # Me > Robot (d-pad) > Robot (d-pad) -> Robot (num-pad)

    def part1
      # num_pad_grid_coordinates.each { |k, v| puts("#{k.inspect} -> #{v.inspect}") }
      # num_pad_neighbours.each { |k, v| puts("#{k} -> #{v.inspect}") }
      # d_pad_neighbours.each { |k, v| puts("#{k} -> #{v.inspect}") }

      puts(complexity("029A".chars).inspect)
      # <A^A>^^AvvvA

      # TODO - need to convert paths to directions (e.g. 8 -> 7 = "<" or ^ -> A = ">")
      #
      # Approach:
      # find the shortest path on the numeric pad, converting each path to directions
      # then for every node to node path in the first D pad, work out the shortest path to press each button, again converting each path to directions to press
      # then repeat the same approach again for the outer D pad

      # door_unlock_codes.map { |duc| complexity(duc) }.sum
    end

    def part2
      "TODO"
    end

    def bfs(root, goal, neighbours)
      q = [root]
      explored = [root].to_set
      prev = { root => nil }

      until q.empty?
        v = q.shift
        if v == goal
          node = v
          path = []
          until node.nil?
            path = [node] + path
            node = prev[node]
          end
          return path
        end

        for w in neighbours[v].keys
          unless explored.member?(w)
            explored.add(w)
            prev[w] = v
            q.append(w)
          end
        end
      end

      nil
    end

    def path_to_directions(path, neighbours)
      (1...path.length).map { |i| neighbours[path[i - 1]][path[i]] }
    end

    def complexity(door_unlock_code)
      # describe the path between the door code keys, starting at A
      numeric_paths = ["A"] + door_unlock_code # e.g. for 029A = A -> 0 -> 2 -> 9 -> A
      puts(numeric_paths.join(""))

      # turn those paths into the directions needed to press on a directional key pad by doing a BFS
      # between every button, turning each path into directions (<>^v) and adding an A press to push
      # each button we reach.
      inner_d_pad_paths =
        (1...numeric_paths.length)
          .map { |i| bfs(numeric_paths[i - 1], numeric_paths[i], num_pad_neighbours) }
          .map { |path| path_to_directions(path, num_pad_neighbours) }
          .map { |path| path + ["A"] }
          .flatten
      puts(inner_d_pad_paths.join(""))

      # we then need to apply this same logic again, but this time we are using a D pad to control
      # the inner D pad of which is used to press the keys on the numeric pad
      mid_d_pad_paths =
        (1...inner_d_pad_paths.length)
          .map { |i| bfs(inner_d_pad_paths[i - 1], inner_d_pad_paths[i], d_pad_neighbours) }
          .map { |path| path_to_directions(path, d_pad_neighbours) }
          .map { |path| path + ["A"] }
          .flatten
      puts(mid_d_pad_paths.join(""))

      outer_d_pad_paths =
        (1...mid_d_pad_paths.length)
          .map { |i| bfs(mid_d_pad_paths[i - 1], mid_d_pad_paths[i], d_pad_neighbours) }
          .map { |path| path_to_directions(path, d_pad_neighbours) }
          .map { |path| path + ["A"] }
          .flatten
      puts(outer_d_pad_paths.join(""))

      door_unlock_code.filter { |ch| ch.match(/^\d$/) }.join("").to_i * outer_d_pad_paths.length
    end

    def num_pad_grid
      @num_pad_grid ||=
        [
          %w[7 8 9],
          %w[4 5 6],
          %w[1 2 3],
          %w[# 0 A]
        ]
    end

    def num_pad_neighbours
      @num_pad_neighbours ||=
        num_pad_grid_coordinates
          .map { |button, point|
            steps =
              DIRECTIONS.map { |direction, delta|
                neighbour_point = apply_delta(point, delta)
                neighbour_key = num_pad_key_at(neighbour_point)
                [neighbour_key, direction]
              }.reject { |k, _| k.nil? }.to_h

            [button, steps]
          }.to_h
    end

    def num_pad_width
      @num_pad_width ||= num_pad_grid[0].length
    end

    def num_pad_height
      @num_pad_height ||= num_pad_grid.length
    end

    def num_pad_grid_coordinates
      @num_pad_grid_coordinates ||=
        (0..num_pad_height - 1)
          .flat_map { |y| (0..num_pad_width - 1).map { |x| [x, y] } }
          .reject { |point| num_pad_key_at(point).nil? }
          .map { |point| [num_pad_key_at(point), point] }
          .to_h
    end

    def in_num_pad_bounds?(point)
      x, y = point
      x >= 0 && x < num_pad_width && y >= 0 && y < num_pad_height
    end

    def num_pad_key_at(point)
      if !in_num_pad_bounds?(point)
        nil
      else
        x, y = point
        row = num_pad_grid[y] || []
        val = row[x] || nil
        if val == '#'
          nil
        else
          val
        end
      end
    end

    def d_pad_grid
      @d_pad_grid ||=
        [
          %w[# ^ A],
          %w[< v >]
        ]
    end

    def d_pad_width
      @d_pad_width ||= d_pad_grid[0].length
    end

    def d_pad_height
      @d_pad_height ||= d_pad_grid.length
    end

    def d_pad_grid_coordinates
      @d_pad_grid_coordinates ||=
        (0..d_pad_height - 1)
          .flat_map { |y| (0..d_pad_width - 1).map { |x| [x, y] } }
          .reject { |point| d_pad_key_at(point).nil? }
          .map { |point| [d_pad_key_at(point), point] }
          .to_h
    end

    def d_pad_neighbours
      @d_pad_neighbours ||=
        d_pad_grid_coordinates
          .map { |button, point|
            steps =
              DIRECTIONS.map { |direction, delta|
                neighbour_point = apply_delta(point, delta)
                neighbour_key = d_pad_key_at(neighbour_point)
                [neighbour_key, direction]
              }.reject { |k, _| k.nil? }.to_h

            [button, steps]
          }.to_h
    end

    def d_pad_key_at(point)
      if !in_d_pad_bounds?(point)
        nil
      else
        x, y = point
        row = d_pad_grid[y] || []
        val = row[x] || nil
        if val == '#'
          nil
        else
          val
        end
      end
    end

    def in_d_pad_bounds?(point)
      x, y = point
      x >= 0 && x < d_pad_width && y >= 0 && y < d_pad_height
    end

    def door_unlock_codes
      @door_unlock_codes ||= lines.map { |l| l.strip.chars }
    end

    def apply_delta(point, delta)
      x, y = point
      dx, dy = delta
      [x + dx, y + dy]
    end

    def direction_to_delta(direction)
      case direction
      when "^"
        [0, -1]
      when ">"
        [1, 0]
      when "v"
        [0, 1]
      when "<"
        [-1, 0]
      else
        raise ArgumentError, "invalid direction #{direction}"
      end
    end
  end
end