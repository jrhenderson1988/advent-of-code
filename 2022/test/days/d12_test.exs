defmodule AoC.Days.D12Test do
  use ExUnit.Case
  doctest AoC

  describe "day 12" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d12.txt") |> File.read!()
      result = AoC.Days.D12.part_one(input)

      assert result == {:ok, 31}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d12.txt") |> File.read!()
    #   result = AoC.Days.D12.part_two(input)

    #   assert result == {:ok, 2713310158}
    # end
  end
end
