defmodule AoC.Days.D03Test do
  use ExUnit.Case
  doctest AoC

  describe "day 1" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d03.txt") |> File.read!()
      result = AoC.Days.D03.part_one(input)

      assert result == {:ok, 157}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d03.txt") |> File.read!()
      result = AoC.Days.D03.part_two(input)

      assert result == {:ok, 70}
    end
  end
end
