defmodule AoC.Days.D08Test do
  use ExUnit.Case
  doctest AoC

  describe "day 8" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d08.txt") |> File.read!()
      result = AoC.Days.D08.part_one(input)
      assert result == {:ok, 21}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d08.txt") |> File.read!()
      result = AoC.Days.D08.part_two(input)
      assert result == {:ok, 8}
    end
  end
end
