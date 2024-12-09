module Aoc
  class Day
    def initialize(content)
      @content = content
    end

    def lines
      @lines ||= @content.strip.lines.map { |line| line.strip }
    end

    def content
      @content
    end
  end
end