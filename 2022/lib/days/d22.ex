defmodule AoC.Days.D22 do
  def part_one(content) do
    result = parse(content) |> simulate() |> get_password()

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp simulate({{world, start}, instructions}) do
    instructions
    |> Enum.reduce({start, :right}, fn instruction, {pos, facing} ->
      case instruction do
        :right -> {pos, turn(facing, :right)}
        :left -> {pos, turn(facing, :left)}
        distance -> {walk(pos, facing, distance, world), facing}
      end
    end)
  end

  defp turn(current, direction) do
    case direction do
      :right ->
        case current do
          :right -> :down
          :down -> :left
          :left -> :up
          :up -> :right
        end

      :left ->
        case current do
          :right -> :up
          :down -> :right
          :left -> :down
          :up -> :left
        end
    end
  end

  defp walk(pos, facing, distance, world) do
    case facing do
      :right ->
        {min_x, max_x} = get_row_boundaries(pos, world)

        take_steps(world, pos, distance, fn {x, y} ->
          {if(x == max_x, do: min_x, else: x + 1), y}
        end)

      :down ->
        {min_y, max_y} = get_column_boundaries(pos, world)

        take_steps(world, pos, distance, fn {x, y} ->
          {x, if(y == max_y, do: min_y, else: y + 1)}
        end)

      :left ->
        {min_x, max_x} = get_row_boundaries(pos, world)

        take_steps(world, pos, distance, fn {x, y} ->
          {if(x == min_x, do: max_x, else: x - 1), y}
        end)

      :up ->
        {min_y, max_y} = get_column_boundaries(pos, world)

        take_steps(world, pos, distance, fn {x, y} ->
          {x, if(y == min_y, do: max_y, else: y - 1)}
        end)

      _ ->
        :error
    end
  end

  defp take_steps(world, pos, distance, next_pos_fn) do
    1..distance
    |> Enum.reduce_while(pos, fn _, {x, y} ->
      new_pos = next_pos_fn.({x, y})

      case Map.get(world, new_pos) do
        :wall -> {:halt, {x, y}}
        :tile -> {:cont, new_pos}
        nil -> :error
      end
    end)
  end

  defp get_row_boundaries({x, y}, world, step \\ 0) do
    case step do
      0 ->
        {min_x, _} = get_row_boundaries({x, y}, world, -1)
        {_, max_x} = get_row_boundaries({x, y}, world, 1)
        {min_x, max_x}

      1 ->
        case Map.get(world, {x, y}) do
          nil -> {:unused, x - 1}
          _ -> get_row_boundaries({x + 1, y}, world, 1)
        end

      -1 ->
        case Map.get(world, {x, y}) do
          nil -> {x + 1, :unused}
          _ -> get_row_boundaries({x - 1, y}, world, -1)
        end
    end
  end

  defp get_column_boundaries({x, y}, world, step \\ 0) do
    case step do
      0 ->
        {min_y, _} = get_column_boundaries({x, y}, world, -1)
        {_, max_y} = get_column_boundaries({x, y}, world, 1)
        {min_y, max_y}

      1 ->
        case Map.get(world, {x, y}) do
          nil -> {:unused, y - 1}
          _ -> get_column_boundaries({x, y + 1}, world, 1)
        end

      -1 ->
        case Map.get(world, {x, y}) do
          nil -> {y + 1, :unused}
          _ -> get_column_boundaries({x, y - 1}, world, -1)
        end
    end
  end

  defp get_password({pos, facing}) do
    {x, y} = pos

    facing_value =
      case facing do
        :right -> 0
        :down -> 1
        :left -> 2
        :up -> 3
      end

    col = y + 1
    row = x + 1
    1000 * col + 4 * row + facing_value
  end

  defp parse(content) do
    [first, second] = AoC.Common.split_double_lines(content)
    {parse_world(first), parse_instructions(second)}
  end

  defp parse_world(content) do
    AoC.Common.split_lines(content)
    |> Enum.with_index()
    |> Enum.reduce({%{}, nil}, fn {line, y}, {world, start} ->
      String.trim_trailing(line)
      |> AoC.Common.characters()
      |> Enum.with_index()
      |> Enum.reduce({world, start}, fn {ch, x}, {world, start} ->
        case ch do
          ?\s -> {world, start}
          ?. -> {Map.put(world, {x, y}, :tile), if(start == nil, do: {x, y}, else: start)}
          ?# -> {Map.put(world, {x, y}, :wall), start}
        end
      end)
    end)
  end

  defp parse_instructions(content) do
    {instructions, capture} =
      String.trim(content)
      |> AoC.Common.characters()
      |> Enum.reduce({[], []}, fn ch, {instructions, capture} ->
        case ch do
          ?R -> {instructions ++ [parse_int(capture), :right], []}
          ?L -> {instructions ++ [parse_int(capture), :left], []}
          ch -> {instructions, capture ++ [ch]}
        end
      end)

    cond do
      length(capture) > 0 -> instructions ++ [parse_int(capture)]
      true -> instructions
    end
  end

  defp parse_int(chars) do
    {i, _} = List.to_string(chars) |> Integer.parse()
    i
  end
end
