defmodule AoC.Days.D09Test do
  use ExUnit.Case
  doctest AoC

  describe "day 9" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d09.txt") |> File.read!()
      result = AoC.Days.D09.part_one(input)
      assert result == {:ok, 13}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d09.txt") |> File.read!()
    #   result = AoC.Days.D09.part_two(input)
    #   assert result == {:ok, 24933642}
    # end
  end
end
