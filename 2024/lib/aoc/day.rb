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

    def chunks
      @chunks ||= content.gsub(/\r\n|\r|\n/, "\n").split("\n\n")
    end

    def test?
      @test
    end
  end
end