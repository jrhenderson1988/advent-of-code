defmodule AoC.Days.D04Test do
  use ExUnit.Case
  doctest AoC

  describe "day 1" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d04.txt") |> File.read!()
      result = AoC.Days.D04.part_one(input)

      assert result == {:ok, 2}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d04.txt") |> File.read!()
      result = AoC.Days.D04.part_two(input)

      assert result == {:ok, 4}
    end
  end
end
