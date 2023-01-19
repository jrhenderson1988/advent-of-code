defmodule AoC.Days.D24Test do
  use ExUnit.Case
  doctest AoC

  describe "day 24" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d24.txt") |> File.read!()
      result = AoC.Days.D24.part_one(input)

      assert result == {:ok, 18}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d24.txt") |> File.read!()
      result = AoC.Days.D24.part_two(input)

      assert result == {:ok, 54}
    end
  end
end
