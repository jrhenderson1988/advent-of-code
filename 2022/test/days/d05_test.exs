defmodule AoC.Days.D05Test do
  use ExUnit.Case
  doctest AoC

  describe "day 5" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d05.txt") |> File.read!()
      result = AoC.Days.D05.part_one(input)

      assert result == {:ok, "CMZ"}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d05.txt") |> File.read!()
      result = AoC.Days.D05.part_two(input)

      assert result == {:ok, "MCD"}
    end
  end
end
