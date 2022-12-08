defmodule AoC.Days.D07Test do
  use ExUnit.Case
  doctest AoC

  describe "day 7" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d07.txt") |> File.read!()
      result = AoC.Days.D07.part_one(input)
      assert result == {:ok, 95437}
    end

    test "part 2" do
      input = Path.expand("./test/days/examples/d07.txt") |> File.read!()
      result = AoC.Days.D07.part_two(input)
      assert result == {:ok, 24933642}
    end
  end
end
