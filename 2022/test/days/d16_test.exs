defmodule AoC.Days.D16Test do
  use ExUnit.Case
  doctest AoC

  describe "day 16" do
    test "part 1" do
      # IO.puts("REMINDER: Day 16 is not yet complete")
      input = Path.expand("./test/days/examples/d16.txt") |> File.read!()
      result = AoC.Days.D16.part_one(input)

      assert result == {:ok, 1651}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d16.txt") |> File.read!()
    #   result = AoC.Days.D16.part_two(input)

    #   assert result == {:ok, 56_000_011}
    # end
  end
end
