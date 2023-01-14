defmodule AoC.Days.D20 do
  def part_one(content) do
    result =
      parse(content)
      |> mix(1)
      |> get_grove_coordinates()
      |> Enum.sum()

    {:ok, result}
  end

  def part_two(content) do
    result =
      parse(content)
      |> Enum.map(fn n -> n * 811_589_153 end)
      |> mix(10)
      |> get_grove_coordinates()
      |> Enum.sum()

    {:ok, result}
  end

  defp parse(content) do
    AoC.Common.split_lines(content)
    |> Enum.map(fn line ->
      {i, _} = String.trim(line) |> Integer.parse()
      i
    end)
  end

  defp mix(numbers, times) do
    len = length(numbers)
    numbers = Enum.with_index(numbers)

    0..(times - 1)
    |> Enum.reduce(numbers, fn _, numbers ->
      0..(len - 1)
      |> Enum.reduce(numbers, fn i, numbers ->
        pos = Enum.find_index(numbers, fn {_, idx} -> i == idx end)
        {num, idx} = Enum.at(numbers, pos)
        new_pos = new_position(len, pos, num)

        numbers |> List.delete_at(pos) |> List.insert_at(new_pos, {num, idx})
      end)
    end)
    |> to_numbers()
  end

  defp get_grove_coordinates(numbers) do
    zero_idx = Enum.find_index(numbers, fn num -> num == 0 end)

    [1000, 2000, 3000]
    |> Enum.map(fn idx -> rem(zero_idx + idx, length(numbers)) end)
    |> Enum.map(fn idx -> Enum.at(numbers, idx) end)
  end

  defp to_numbers(numbers) do
    Enum.map(numbers, fn {n, _} -> n end)
  end

  defp new_position(len, pos, value) do
    move_size = Integer.mod(value, len - 1)
    offset = if pos + move_size >= len, do: 1, else: 0
    Integer.mod(pos + move_size, len) + offset
  end
end
