defmodule AoC.Days.D04 do
  def part_one(content) do
    result =
      AoC.Common.split_lines(content)
      |> Enum.map(&parse_assignment_pairs/1)
      |> Enum.filter(&contains_total_overlap/1)
      |> Enum.count()

    {:ok, result}
  end

  def part_two(content) do
    result =
      AoC.Common.split_lines(content)
      |> Enum.map(&parse_assignment_pairs/1)
      |> Enum.filter(&contains_any_overlap/1)
      |> Enum.count()

    {:ok, result}
  end

  defp parse_assignment_pairs(line) do
    String.split(line, ",") |> Enum.map(&parse_assignment_pair/1)
  end

  defp parse_assignment_pair(input) do
    [first, second] =
      String.split(input, "-")
      |> Enum.map(fn entry ->
        {int, _} = Integer.parse(entry)
        int
      end)

    cond do
      second > first -> first..second
      true -> second..first
    end
  end

  defp contains_total_overlap([a, b] = _pair) do
    [larger, smaller] = cond do
      Range.size(a) > Range.size(b) -> [a, b]
      true -> [b, a]
    end

    Enum.all?(smaller, fn num -> num in larger end)
  end

  defp contains_any_overlap([a, b] = _pair) do
    [larger, smaller] = cond do
      Range.size(a) > Range.size(b) -> [a, b]
      true -> [b, a]
    end

    Enum.any?(smaller, fn num -> num in larger end)
  end
end
