defmodule AoC.Days.D15Test do
  use ExUnit.Case
  doctest AoC

  describe "day 15" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d15.txt") |> File.read!()
      result = AoC.Days.D15.part_one(input, 10)

      assert result == {:ok, 26}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d15.txt") |> File.read!()
    #   result = AoC.Days.D15.part_two(input)

    #   assert result == {:ok, 93}
    # end
  end
end
