defmodule AoC.Days.D20Test do
  use ExUnit.Case
  doctest AoC

  describe "day 20" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d20.txt") |> File.read!()
      result = AoC.Days.D20.part_one(input)

      assert result == {:ok, 3}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d20.txt") |> File.read!()
      result = AoC.Days.D20.part_two(input)

      assert result == {:ok, 1_623_178_306}
    end
  end
end
