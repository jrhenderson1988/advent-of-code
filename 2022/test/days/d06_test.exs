defmodule AoC.Days.D06Test do
  use ExUnit.Case
  doctest AoC

  describe "day 6" do
    test "part 1" do
      tests = [
        {"mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7},
        {"bvwbjplbgvbhsrlpgdmjqwftvncz", 5},
        {"nppdvjthqldpwncqszvftbrmjlhg", 6},
        {"nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10},
        {"zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11},
      ]

      for {input, expected} <- tests do
        result = AoC.Days.D06.part_one(input)
        assert result == {:ok, expected}
      end
    end

    test "part 2" do
      tests = [
        {"mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19},
        {"bvwbjplbgvbhsrlpgdmjqwftvncz", 23},
        {"nppdvjthqldpwncqszvftbrmjlhg", 23},
        {"nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29},
        {"zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26},
      ]

      for {input, expected} <- tests do
        result = AoC.Days.D06.part_two(input)
        assert result == {:ok, expected}
      end
    end
  end
end
