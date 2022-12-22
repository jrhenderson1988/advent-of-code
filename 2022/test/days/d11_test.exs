defmodule AoC.Days.D11Test do
  use ExUnit.Case
  doctest AoC

  describe "day 11" do
    test "part 1" do
      input = Path.expand("./test/days/examples/d11.txt") |> File.read!()
      result = AoC.Days.D11.part_one(input)

      assert result == {:ok, 10605}
    end

    # test "part 2" do
    #   input = Path.expand("./test/days/examples/d11.txt") |> File.read!()
    #   result = AoC.Days.D11.part_two(input)

    #   assert result == {:ok, expected}
    # end
  end
end
