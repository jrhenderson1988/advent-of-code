defmodule AoC.Days.D24 do
  def part_one(content) do
    result = parse(content) |> find_length_of_shortest_path()

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp find_length_of_shortest_path({start, finish, bounds, blizzards}) do
    search(MapSet.new([start]), finish, bounds, blizzards, 0)
  end

  defp search(queue, target, bounds, blizzards, step) do
    cond do
      MapSet.member?(queue, target) ->
        step

      true ->
        blizzards = move_blizzards(bounds, blizzards)
        blizzard_locations = get_blizzard_locations(blizzards)

        new_queue =
          queue
          |> Enum.flat_map(fn {x, y} ->
            [{x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}, {x, y}]
            |> Enum.filter(fn point -> in_bounds(bounds, point) end)
            |> Enum.reject(fn point -> MapSet.member?(blizzard_locations, point) end)
          end)
          |> MapSet.new()

        search(new_queue, target, bounds, blizzards, step + 1)
    end
  end

  defp move_blizzards(bounds, blizzards) do
    blizzards
    |> Enum.map(fn {x, y, dir} ->
      {min_y, max_y} = get_col_bounds(bounds, x)
      {min_x, max_x} = get_row_bounds(bounds, y)

      cond do
        dir == :up and y == min_y -> {x, max_y, dir}
        dir == :up -> {x, y - 1, dir}
        dir == :down and y == max_y -> {x, min_y, dir}
        dir == :down -> {x, y + 1, dir}
        dir == :left and x == min_x -> {max_x, y, dir}
        dir == :left -> {x - 1, y, dir}
        dir == :right and x == max_x -> {min_x, y, dir}
        dir == :right -> {x + 1, y, dir}
      end
    end)
  end

  defp get_blizzard_locations(blizzards) do
    blizzards |> Enum.map(fn {x, y, _} -> {x, y} end) |> MapSet.new()
  end

  defp get_row_bounds(bounds, row) do
    {row_bounds, _} = bounds
    Map.get(row_bounds, row)
  end

  defp get_col_bounds(bounds, col) do
    {_, col_bounds} = bounds
    Map.get(col_bounds, col)
  end

  defp in_bounds(bounds, {x, y}) do
    case get_row_bounds(bounds, y) do
      nil ->
        false

      {min_x, max_x} ->
        case get_col_bounds(bounds, x) do
          nil ->
            false

          {min_y, max_y} ->
            x in min_x..max_x and y in min_y..max_y
        end
    end
  end

  defp parse(content) do
    String.trim(content)
    |> AoC.Common.split_lines()
    |> Enum.with_index()
    |> Enum.reduce({nil, nil, {%{}, %{}}, []}, fn {line, y}, {start, finish, bounds, blizzards} ->
      String.trim(line)
      |> AoC.Common.characters()
      |> Enum.with_index()
      |> Enum.reduce({start, finish, bounds, blizzards}, fn {ch, x},
                                                            {start, finish, bounds, blizzards} ->
        {start, finish, bounds} =
          case ch do
            ch when ch in [?., ?>, ?<, ?v, ?^] ->
              {row_bounds, col_bounds} = bounds
              new_start = if(start == nil, do: {x, y}, else: start)
              {min_x, max_x} = Map.get(row_bounds, y, {x, x})
              row_bounds = Map.put(row_bounds, y, {min(x, min_x), max(x, max_x)})
              {min_y, max_y} = Map.get(col_bounds, x, {y, y})
              col_bounds = Map.put(col_bounds, x, {min(y, min_y), max(y, max_y)})
              {new_start, {x, y}, {row_bounds, col_bounds}}

            _ ->
              {start, finish, bounds}
          end

        blizzards =
          case ch do
            ?> -> blizzards ++ [{x, y, :right}]
            ?< -> blizzards ++ [{x, y, :left}]
            ?v -> blizzards ++ [{x, y, :down}]
            ?^ -> blizzards ++ [{x, y, :up}]
            _ -> blizzards
          end

        {start, finish, bounds, blizzards}
      end)
    end)
  end
end
