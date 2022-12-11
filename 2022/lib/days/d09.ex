defmodule AoC.Days.D09 do
  def part_one(content) do
    rope = make_rope(2)
    result = parse_instructions(content) |> simulate_and_count_tail_positions(rope)

    {:ok, result}
  end

  def part_two(content) do
    rope = make_rope(10)
    result = parse_instructions(content) |> simulate_and_count_tail_positions(rope)

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

  defp simulate_and_count_tail_positions(instructions, rope) do
    {_, tail_positions} =
      Enum.reduce(instructions, {rope, MapSet.new([{0, 0}])}, fn instruction,
                                                                 {rope, tail_positions} ->
        move_rope(rope, instruction, tail_positions)
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

  defp is_adjacent({ax, ay}, {bx, by}) do
    [{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}]
    |> Enum.any?(fn {dx, dy} -> ax + dx == bx and ay + dy == by end)
  end

  defp make_rope(size) do
    0..(size - 1) |> Enum.map(fn _ -> {0, 0} end)
  end

  defp move_rope(rope, instruction, tail_positions) do
    [head | rest] = rope

    {head, rest, tail_positions} =
      move_head(head, instruction)
      |> Enum.reduce({head, rest, tail_positions}, fn new_head, {_, rest, tail_positions} ->
        rest = move_rest(new_head, rest)
        tail_positions = MapSet.put(tail_positions, Enum.at(rest, -1))

        {new_head, rest, tail_positions}
      end)

    rope = [head | rest]

    {rope, tail_positions}
  end

  defp move_rest(head, rest) do
    {_, new_rest} =
      Enum.reduce(rest, {head, []}, fn current, {neighbour, new_rest} ->
        new_current = move_knot(current, neighbour)
        new_rest = new_rest ++ [new_current]
        {new_current, new_rest}
      end)

    new_rest
  end

  defp move_knot(current, neighbour) do
    cond do
      is_adjacent(current, neighbour) ->
        current

      true ->
        {cx, cy} = current
        {nx, ny} = neighbour

        dx =
          cond do
            cx < nx -> 1
            cx > nx -> -1
            true -> 0
          end

        dy =
          cond do
            cy < ny -> 1
            cy > ny -> -1
            true -> 0
          end

        {cx + dx, cy + dy}
    end
  end
end
