module Aoc
  class Day09 < Day
    def part1
      calculate_checksum(compress(blocks))
    end

    def part2
      calculate_checksum(compress_entire_files(blocks))
    end

    def blocks
      @blocks ||= expand_blocks
    end

    # TODO - this can be used later for optimising if needed
    def build_block_summaries
      values = content.strip.chars.map { |c| c.to_i }
      blocks, _ =
        (0..values.length - 1).reduce([[], 0]) do |acc, i|
          blocks, current_offset = acc
          is_file = i % 2 == 0
          id = i / 2
          memory_size = values[i]

          if is_file
            [blocks + [[id, current_offset, memory_size]], current_offset + memory_size]
          else
            [blocks, current_offset + memory_size]
          end
        end
      blocks
    end

    def expand_blocks
      values = content.strip.chars.map { |c| c.to_i }
      (0..values.length - 1).reduce([]) do |acc, i|
        is_file = i % 2 == 0
        id = i / 2
        memory_size = values[i]

        acc + (0..memory_size - 1).map { |_| is_file ? id : nil }
      end
    end

    def compress(blocks)
      empty_offset = 0
      end_offset = blocks.length - 1
      while empty_offset < end_offset
        if !blocks[empty_offset].nil?
          empty_offset += 1
        elsif blocks[end_offset].nil?
          end_offset -= 1
        else
          blocks[empty_offset] = blocks[end_offset]
          blocks[end_offset] = nil
          empty_offset += 1
          end_offset -= 1
        end
      end

      blocks
    end

    def compress_entire_files(blocks)
      empty_offset = 0
      file_offset = blocks.length - 1

      while file_offset > 0
        file_offset, file_length = find_last_file(blocks, file_offset)
        space_offset = find_next_contiguous_space_of_size(blocks, empty_offset, file_length)
        if !space_offset.nil? && space_offset < file_offset
          for i in (0..file_length - 1)
            blocks[space_offset + i] = blocks[file_offset + i]
            blocks[file_offset + i] = nil
          end
          file_offset -= 1
        else
          file_offset -= 1
        end
      end

      blocks
    end

    def calculate_checksum(blocks)
      (0..blocks.length - 1).filter { |i| !blocks[i].nil? }.map { |i| i * blocks[i] }.sum
    end

    def find_last_file(blocks, offset)
      current = offset
      length = 0
      current_id = blocks[current]

      while current > 0
        if length == 0 && blocks[current].nil?
          # we're not looking at a file and the current space is empty, move offset back
          current -= 1
        elsif length == 0 && !blocks[current].nil?
          # we've found the end of a file, keep scanning back for the start and record the length
          current_id = blocks[current]
          length = 1
          current -= 1
        elsif length > 0 && (blocks[current].nil? || blocks[current] != current_id)
          # we've just gone past the start of the file, the offset should be increased by one, so
          # that it points at the first block in the file, and we should break from the loop.
          current += 1
          break
        elsif length > 0 && !blocks[current].nil?
          # we're still scanning within a file region, increase size by 1
          length += 1
          current -= 1
        end
      end

      # edge case at the end
      if current == 0 && blocks[0] == blocks[1]
        length += 1
      end

      [current, length]
    end

    # offset is assumed to currently point to empty space
    def find_next_contiguous_space_of_size(blocks, offset, target_size)
      current = offset
      target = (0..target_size - 1).map { |_| nil }
      while current < blocks.length
        if blocks[current..(current + target_size - 1)] == target
          return current
        end
        current += 1
      end

      nil
    end
  end
end

