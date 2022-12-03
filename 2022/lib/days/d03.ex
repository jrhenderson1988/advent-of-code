defmodule AoC.Days.D03 do
  def part_one(content) do
    result =
      AoC.Common.split_lines(content)
      |> Enum.map(fn line ->
        with {:ok, char} <- find_common_character(line),
             {:ok, value} <- get_character_priority(char) do
          value
        end
      end)
      |> Enum.sum()

    {:ok, result}
  end

  def part_two(content) do
    result =
      AoC.Common.split_lines(content)
      |> Enum.chunk_every(3)
      |> Enum.map(fn chunk ->
        with {:ok, common_character} <- find_common_character_in_group(chunk),
             {:ok, priority} <- get_character_priority(common_character) do
          priority
        end
      end)
      |> Enum.sum()

    {:ok, result}
  end

  defp find_common_character(line) do
    chars = String.to_charlist(line)
    len = length(chars)

    case rem(len, 2) do
      1 ->
        {:error, :uneven_rucksack}

      0 ->
        first_compartment = Enum.slice(chars, 0..(div(len, 2) - 1)) |> MapSet.new()
        second_compartment = Enum.slice(chars, div(len, 2)..-1) |> MapSet.new()

        intersection =
          MapSet.intersection(first_compartment, second_compartment) |> MapSet.to_list()

        case length(intersection) do
          1 -> {:ok, Enum.at(intersection, 0)}
          _ -> {:error, :no_single_common_character}
        end
    end
  end

  defp find_common_character_in_group(chunks) do
    intersection =
      chunks
      |> Enum.map(fn chunk -> String.to_charlist(chunk) end)
      |> Enum.map(fn chars -> MapSet.new(chars) end)
      |> Enum.reduce(fn elem, acc -> MapSet.intersection(elem, acc) end)
      |> MapSet.to_list()

    case length(intersection) do
      1 -> {:ok, Enum.at(intersection, 0)}
      _ -> {:error, :no_single_common_character}
    end
  end

  defp get_character_priority(character) do
    cond do
      character in 97..122 -> {:ok, character - 96}
      character in 65..90 -> {:ok, character - 38}
      true -> {:error, :invalid_character}
    end
  end
end
