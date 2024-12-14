module Aoc
  class Day
    def initialize(content, test)
      @content = content.strip
      @test = test
    end

    def lines
      @lines ||= @content.strip.lines.map { |line| line.strip }
    end

    def content
      @content
    end

    def test?
      @test
    end
  end
end