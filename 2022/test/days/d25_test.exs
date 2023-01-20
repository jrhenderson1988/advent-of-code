defmodule AoC.Days.D25Test do
  use ExUnit.Case
  doctest AoC

  describe "day 25" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d25.txt") |> File.read!()
      result = AoC.Days.D25.part_one(input)

      assert result == {:ok, "2=-1=0"}
    end
  end
end
