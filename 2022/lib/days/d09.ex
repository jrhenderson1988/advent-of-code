defmodule AoC.Days.D09 do
  def part_one(content) do
    result = parse_instructions(content) |> simulate_and_count_tail_positions()

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp parse_instructions(content) do
    AoC.Common.split_lines(content)
    |> Enum.map(fn line ->
      [direction, amount] = String.split(line, " ")
      {amt, _} = Integer.parse(amount)

      case direction do
        "L" -> {:left, amt}
        "R" -> {:right, amt}
        "U" -> {:up, amt}
        "D" -> {:down, amt}
      end
    end)
  end

  defp simulate_and_count_tail_positions(instructions) do
    {_, _, tail_positions} =
      Enum.reduce(instructions, {{0, 0}, {0, 0}, MapSet.new([{0, 0}])}, fn instruction,
                                                                           {head, tail,
                                                                            tail_positions} ->
        move_head(head, instruction)
        |> Enum.reduce({head, tail, tail_positions}, fn new_head, {_, tail, tail_positions} ->
          new_tail = move_tail(tail, new_head)
          {new_head, new_tail, MapSet.put(tail_positions, new_tail)}
        end)
      end)

    MapSet.size(tail_positions)
  end

  defp move_head(current, instruction) do
    {x, y} = current
    {direction, amount} = instruction

    {dx, dy} =
      case direction do
        :left -> {-1, 0}
        :right -> {1, 0}
        :up -> {0, 1}
        :down -> {0, -1}
      end

    1..amount |> Enum.map(fn n -> {x + dx * n, y + dy * n} end)
  end

  defp move_tail(current, head) do
    cond do
      is_neighbour(current, head) ->
        current

      true ->
        {tx, ty} = current
        {hx, hy} = head

        dx =
          cond do
            tx < hx -> 1
            tx > hx -> -1
            true -> 0
          end

        dy =
          cond do
            ty < hy -> 1
            ty > hy -> -1
            true -> 0
          end

        {tx + dx, ty + dy}
    end
  end

  defp is_neighbour({ax, ay}, {bx, by}) do
    [{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}]
    |> Enum.any?(fn {dx, dy} -> ax + dx == bx and ay + dy == by end)
  end
end
