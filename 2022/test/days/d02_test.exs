defmodule AoC.Days.D02Test do
  use ExUnit.Case
  doctest AoC

  describe "day 1" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d02.txt") |> File.read!()
      result = AoC.Days.D02.part_one(input)

      assert result == {:ok, 15}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d02.txt") |> File.read!()
      result = AoC.Days.D02.part_two(input)

      assert result == {:ok, 12}
    end
  end
end
