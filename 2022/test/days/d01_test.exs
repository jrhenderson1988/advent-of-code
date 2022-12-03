defmodule AoC.Days.D01Test do
  use ExUnit.Case
  doctest AoC

  describe "day 1" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d01.txt") |> File.read!()
      result = AoC.Days.D01.part_one(input)

      assert result == {:ok, 24000}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d01.txt") |> File.read!()
      result = AoC.Days.D01.part_two(input)

      assert result == {:ok, 45000}
    end
  end
end
