module Aoc
  class Day04 < Day
    def part1
      coordinates.flat_map { |coord| directions.map { |dir| [coord, dir] } }
                 .filter { |check| matches_word(check[0], check[1], "XMAS") }
                 .length
    end

    def part2
      coordinates.filter { |coord| has_mas_in_x_shape(coord) }
                 .size
    end

    def grid
      @grid ||= @content.strip.lines.map { |line| line.strip.chars }
    end

    def directions
      @directions ||= [-1, 0, 1].flat_map { |x| [-1, 0, 1].map { |y| [x, y] } }
                                .reject { |dir| dir[0] == 0 && dir[1] == 0 }
    end

    def coordinates
      @coordinates ||= (0..grid.length - 1).flat_map { |y| (0..grid[0].length - 1).map { |x| [x, y] } }
    end

    def matches_word(coord, direction, expected)
      (0..expected.length - 1).all? { |i| check_letter(coord, direction, i, expected[i]) }
    end

    def check_letter(coord, direction, position, expected_letter)
      x = coord[0] + (position * direction[0])
      y = coord[1] + (position * direction[1])
      letter_at(x, y) == expected_letter
    end

    def has_mas_in_x_shape(coord)
      x = coord[0]
      y = coord[1]

      if letter_at(x, y) != 'A'
        false
      else
        checks = [
          [[x - 1, y - 1], [1, 1]], # ↘
          [[x - 1, y + 1], [1, -1]], # ↗
          [[x + 1, y + 1], [-1, -1]], # ↖
          [[x + 1, y - 1], [-1, 1]], # ↙
        ]

        checks.filter { |check| matches_word(check[0], check[1], "MAS") }.length == 2
      end
    end

    def letter_at(x, y)
      if y < 0 || x < 0 || y >= grid.length || x >= grid[0].length
        nil
      else
        grid[y][x]
      end
    end
  end
end