module Aoc
  class Day05 < Day
    def part1
      updates.filter { |update| valid_update?(update) }
             .map { |update| middle_page(update) }
             .sum
    end

    def part2
      updates.reject { |update| valid_update?(update) }
             .map { |update| fix_update(update) }
             .map { |update| middle_page(update) }
             .sum
    end

    def parse
      chunks = @content.strip.split("\n\n")
      rules_chunk = chunks[0]
      updates_chunk = chunks[1]

      rules = rules_chunk.strip.lines
                         .map { |line| line.strip.split(/\|/).map { |v| v.to_i } }
                         .reduce({}) { |map, pair|
                           map[pair[0]] = (map[pair[0]] || []) + [pair[1]]
                           map
                         }
      updates = updates_chunk.strip.lines.map { |line| line.strip.split(/,/).map { |v| v.to_i } }

      [rules, updates]
    end

    def data
      @data ||= parse
    end

    def rules
      data[0]
    end

    def updates
      data[1]
    end

    def valid_update?(update)
      (0..update.length - 1).all? do |idx|
        page = update[idx]
        forbidden_pages = forbidden_preceding_pages(page)
        (0..idx - 1).map { |i| update[i] }
                    .none? { |other_page| forbidden_pages.member?(other_page) }
      end
    end

    def middle_page(update)
      update[update.length / 2]
    end

    def fix_update(update)
      update.sort { |a, b| compare_pages(a, b) }
    end

    def compare_pages(a, b)
      forbidden_pages_a = forbidden_preceding_pages(a)
      forbidden_pages_b = forbidden_preceding_pages(b)

      if forbidden_pages_a.member?(b)
        -1
      elsif forbidden_pages_b.member?(a)
        1
      else
        0
      end
    end

    def forbidden_preceding_pages(page)
      rules[page] || []
    end
  end
end