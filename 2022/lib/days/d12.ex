defmodule AoC.Days.D12 do
  def part_one(content) do
    {grid, start, target, max} = parse_grid(content)
    {:ok, path} = find_path_to_end(grid, start, target, max)

    {:ok, length(path) - 1}
  end

  def part_two(content) do
    {grid, _, target, max} = parse_grid(content)
    result = find_steps_from_best_starting_position(grid, target, max)

    {:ok, result}
  end

  defp parse_grid(content) do
    String.trim(content)
    |> AoC.Common.split_lines()
    |> Enum.with_index()
    |> Enum.reduce({%{}, nil, nil, {0, 0}}, fn {line, y}, {grid, start, target, max} ->
      String.trim(line)
      |> AoC.Common.characters()
      |> Enum.with_index()
      |> Enum.reduce({grid, start, target, max}, fn {ch, x},
                                                    {grid, start, target, {max_x, max_y}} ->
        max_x = if x > max_x, do: x, else: max_x
        max_y = if y > max_y, do: y, else: max_y

        case ch do
          ?S ->
            {Map.put(grid, {x, y}, ?a), {x, y}, target, {max_x, max_y}}

          ?E ->
            {Map.put(grid, {x, y}, ?z), start, {x, y}, {max_x, max_y}}

          letter ->
            {Map.put(grid, {x, y}, letter), start, target, {max_x, max_y}}
        end
      end)
    end)
  end

  defp find_path_to_end(grid, start, target, {max_x, max_y}) do
    AoC.Common.Dijkstra.find_path(Map.keys(grid), start, target, fn {x, y}, _ ->
      [{-1, 0}, {1, 0}, {0, -1}, {0, 1}]
      |> Enum.map(fn {ox, oy} -> {x + ox, y + oy} end)
      |> Enum.filter(fn {x, y} -> x >= 0 and x <= max_x and y >= 0 and y <= max_y end)
      |> Enum.filter(fn coord ->
        curr_height = Map.get(grid, {x, y})
        othr_height = Map.get(grid, coord)

        diff = othr_height - curr_height
        diff <= 1
      end)
      |> Enum.reduce(%{}, fn coord, map -> Map.put(map, coord, 1) end)
    end)
  end

  defp find_steps_from_best_starting_position(grid, target, max) do
    possible_starting_positions =
      grid
      |> Enum.filter(fn {_, height} -> height == ?a end)
      |> Enum.map(fn {coord, _} -> coord end)

    steps =
      possible_starting_positions
      |> Enum.reduce(nil, fn start, best ->
        case find_path_to_end(grid, start, target, max) do
          {:ok, path} ->
            steps = length(path) - 1

            cond do
              best == nil -> steps
              steps < best -> steps
              steps >= best -> best
            end

          {:error, _} ->
            best
        end
      end)

    steps
  end
end
