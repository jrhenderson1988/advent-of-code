defmodule AoC.Days.D10Test do
  use ExUnit.Case
  doctest AoC

  describe "day 10" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d10.txt") |> File.read!()
      result = AoC.Days.D10.part_one(input)
      assert result == {:ok, 13140}
    end
  end
end
