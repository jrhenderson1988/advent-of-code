defmodule AoC.Days.D22Test do
  use ExUnit.Case
  doctest AoC

  describe "day 22" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d22.txt") |> File.read!()
      result = AoC.Days.D22.part_one(input)

      assert result == {:ok, 6032}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d22.txt") |> File.read!()
    #   result = AoC.Days.D22.part_two(input)

    #   assert result == {:ok, 5031}
    # end
  end
end
