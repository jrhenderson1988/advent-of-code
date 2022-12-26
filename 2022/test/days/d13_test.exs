defmodule AoC.Days.D13Test do
  use ExUnit.Case
  doctest AoC

  describe "day 13" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d13.txt") |> File.read!()
      result = AoC.Days.D13.part_one(input)

      assert result == {:ok, 13}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d13.txt") |> File.read!()
      result = AoC.Days.D13.part_two(input)

      assert result == {:ok, 140}
    end
  end
end
