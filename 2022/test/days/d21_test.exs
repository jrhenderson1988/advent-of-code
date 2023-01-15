defmodule AoC.Days.D21Test do
  use ExUnit.Case
  doctest AoC

  describe "day 21" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d21.txt") |> File.read!()
      result = AoC.Days.D21.part_one(input)

      assert result == {:ok, 152}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d21.txt") |> File.read!()
      result = AoC.Days.D21.part_two(input)

      assert result == {:ok, 301}
    end
  end
end
