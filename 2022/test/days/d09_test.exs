defmodule AoC.Days.D09Test do
  use ExUnit.Case
  doctest AoC

  describe "day 9" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d09.txt") |> File.read!()
      result = AoC.Days.D09.part_one(input)
      assert result == {:ok, 13}
    end

    test "part 2" do
      tests = [
        {"./test/days/examples/d09.txt", 1},
        {"./test/days/examples/d09_2.txt", 36},
      ]

      for {file, expected} <- tests do
        input = Path.expand(file) |> File.read!()
        result = AoC.Days.D09.part_two(input)
        assert result == {:ok, expected}
      end
    end
  end
end
