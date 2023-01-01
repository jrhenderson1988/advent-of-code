defmodule AoC.Days.D17Test do
  use ExUnit.Case
  doctest AoC

  describe "day 17" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d17.txt") |> File.read!()
      result = AoC.Days.D17.part_one(input)

      assert result == {:ok, 3068}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d17.txt") |> File.read!()
    #   result = AoC.Days.D17.part_two(input)

    #   assert result == {:ok, 1707}
    # end
  end
end
