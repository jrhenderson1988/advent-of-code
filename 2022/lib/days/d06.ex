defmodule AoC.Days.D06 do
  def part_one(content) do
    {:ok, result} = position_of_distinct_characters(content, 4)

    {:ok, result}
  end

  def part_two(content) do
    {:ok, result} = position_of_distinct_characters(content, 14)

    {:ok, result}
  end

  defp position_of_distinct_characters(message, total_distinct) do
    result =
      message
      |> String.to_charlist()
      |> Enum.chunk_every(total_distinct, 1, :discard)
      |> Enum.with_index(total_distinct)
      |> Enum.find(fn {chunk, _} -> MapSet.size(MapSet.new(chunk)) == total_distinct end)

    case result do
      nil -> {:error, :no_distinct_string_for_length}
      {_, position} -> {:ok, position}
    end
  end
end
