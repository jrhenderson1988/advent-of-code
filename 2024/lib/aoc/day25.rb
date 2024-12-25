module Aoc
  class Day25 < Day
    def part1
      locks.flat_map { |lock| keys.map { |key| [lock, key] } }
           .filter { |pair| fits?(pair[0], pair[1]) }
           .length
    end

    def locks
      @locks ||= chunks.map { |chunk| chunk.strip }
                       .map { |chunk| to_grid(chunk) }
                       .filter { |grid| lock?(grid) }
                       .map { |grid| to_pin_heights(grid) }
    end

    def keys
      @keys ||= chunks.map { |chunk| chunk.strip }
                      .map { |chunk| to_grid(chunk) }
                      .filter { |grid| key?(grid) }
                      .map { |grid| to_pin_heights(grid) }
    end

    def to_grid(chunk)
      chunk.strip.lines.map { |line| line.strip.chars }
    end

    def lock?(grid)
      grid.first.all? { |ch| ch == '#' } && grid.last.all? { |ch| ch == '.' }
    end

    def key?(grid)
      grid.first.all? { |ch| ch == '.' } && grid.last.all? { |ch| ch == '#' }
    end

    def to_pin_heights(grid)
      (0...grid[0].length).map { |x| (0...grid.length).filter { |y| grid[y][x] == '#' }.length - 1 }
    end

    def fits?(lock, key)
      (0...lock.length).all? { |idx| lock[idx] + key[idx] < 6 }
    end

    def part2
      "All done!"
    end
  end
end