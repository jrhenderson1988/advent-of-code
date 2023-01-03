defmodule AoC.Days.D18Test do
  use ExUnit.Case
  doctest AoC

  describe "day 18" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d18.txt") |> File.read!()
      result = AoC.Days.D18.part_one(input)

      assert result == {:ok, 64}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d18.txt") |> File.read!()
      result = AoC.Days.D18.part_two(input)

      assert result == {:ok, 58}
    end
  end
end
