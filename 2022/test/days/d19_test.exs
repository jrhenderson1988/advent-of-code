defmodule AoC.Days.D19Test do
  use ExUnit.Case
  doctest AoC

  describe "day 19" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d19.txt") |> File.read!()
      result = AoC.Days.D19.part_one(input)

      assert result == {:ok, 33}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d19.txt") |> File.read!()
      result = AoC.Days.D19.part_two(input)

      assert result == {:ok, 56 * 62}
    end
  end
end
