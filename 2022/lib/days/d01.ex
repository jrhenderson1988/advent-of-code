defmodule AoC.Days.D01 do
  def part_one(content) do
    result =
      total_calories_by_elf(content)
      |> Enum.max()

    {:ok, result}
  end

  def part_two(content) do
    result =
      total_calories_by_elf(content)
      |> Enum.sort(:desc)
      |> Enum.take(3)
      |> Enum.sum()

    {:ok, result}
  end

  defp total_calories_by_elf(content) do
    AoC.Common.split_double_lines(content)
    |> Enum.map(fn chunk ->
      AoC.Common.split_lines(chunk)
      |> Enum.map(fn line ->
        {value, _} = Integer.parse(line)
        value
      end)
      |> Enum.sum()
    end)
  end
end
