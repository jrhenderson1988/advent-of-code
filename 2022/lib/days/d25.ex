defmodule AoC.Days.D25 do
  def part_one(content) do
    result =
      String.trim(content)
      |> AoC.Common.split_lines()
      |> Enum.map(&to_decimal/1)
      |> Enum.sum()
      |> to_snafu()

    {:ok, result}
  end

  def part_two(_) do
    {:ok, "N/A"}
  end

  defp to_decimal(s) do
    {decimal, _} =
      String.trim(s)
      |> AoC.Common.characters()
      |> Enum.reverse()
      |> Enum.reduce({0, 1}, fn sd, {total, multiplier} ->
        {total + snafu_digit_to_decimal(sd) * multiplier, multiplier * 5}
      end)

    decimal
  end

  defp snafu_digit_to_decimal(sd) do
    case sd do
      ?0 -> 0
      ?1 -> 1
      ?2 -> 2
      ?- -> -1
      ?= -> -2
    end
  end

  defp to_snafu(n) do
    case n do
      0 ->
        ""

      n ->
        {a, b} = {div(n + 2, 5), rem(n + 2, 5)}
        to_snafu(a) <> Enum.at(["=", "-", "0", "1", "2"], b)
    end
  end
end
