defmodule AoC.Days.D23Test do
  use ExUnit.Case
  doctest AoC

  describe "day 23" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d23.txt") |> File.read!()
      result = AoC.Days.D23.part_one(input)

      assert result == {:ok, 110}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d23.txt") |> File.read!()
      result = AoC.Days.D23.part_two(input)

      assert result == {:ok, 20}
    end
  end
end
