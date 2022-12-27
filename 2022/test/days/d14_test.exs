defmodule AoC.Days.D14Test do
  use ExUnit.Case
  doctest AoC

  describe "day 14" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d14.txt") |> File.read!()
      result = AoC.Days.D14.part_one(input)

      assert result == {:ok, 24}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d14.txt") |> File.read!()
    #   result = AoC.Days.D14.part_two(input)

    #   assert result == {:ok, 140}
    # end
  end
end
