defmodule AoC.Days.D22 do
  #         ....
  #         ....
  #         ....
  #         ....
  # ............
  # ............
  # ............
  # ............
  #         ........
  #         ........
  #         ........
  #         ........
  #
  @test_mappings %{
    {2, 0} => %{:left => {:down, {1, 1}}, :up => {:down, {0, 1}}, :right => {:left, {3, 2}}},
    {0, 1} => %{:left => {:up, {3, 2}}, :up => {:down, {2, 0}}, :down => {:up, {2, 2}}},
    {1, 1} => %{:up => {:right, {2, 0}}, :down => {:right, {2, 2}}},
    {2, 1} => %{:right => {:down, {3, 2}}},
    {2, 2} => %{:left => {:up, {1, 1}}, :down => {:up, {0, 1}}},
    {3, 2} => %{:up => {:left, {2, 1}}, :right => {:left, {2, 0}}}
  }

  #    AAABBB
  #    AAABBB
  #    AAABBB
  #    CCC
  #    CCC
  #    CCC
  # DDDEEE
  # DDDEEE
  # DDDEEE
  # FFF
  # FFF
  # FFF
  #
  @input_mappings %{
    # A
    {1, 0} => %{:left => {:right, {0, 2}}, :up => {:right, {0, 3}}},
    # B
    {2, 0} => %{:up => {:up, {0, 3}}, :right => {:left, {1, 2}}, :down => {:left, {1, 1}}},
    # C
    {1, 1} => %{:left => {:down, {0, 2}}, :right => {:up, {2, 0}}},
    # D
    {0, 2} => %{:left => {:right, {1, 0}}, :up => {:right, {1, 1}}},
    # E
    {1, 2} => %{:right => {:left, {2, 0}}, :down => {:left, {0, 3}}},
    # F
    {0, 3} => %{:left => {:down, {1, 0}}, :down => {:down, {2, 0}}, :right => {:up, {1, 2}}}
  }

  def part_one(content) do
    result = parse(content) |> walk_plane() |> get_password()

    {:ok, result}
  end

  def part_two(content, test \\ false) do
    if !test do
      IO.puts("# Warning: Day 22 part 2 is tailored to work only on input of a certain shape.")
      IO.puts("# It is not a general solution and may not work in all cases.")
      IO.puts("# Edit @input_mappings in d22.ex to describe shape and transitions between faces")
    end

    result = parse(content) |> walk_cube(test) |> get_password()

    {:ok, result}
  end

  defp walk_plane({{world, start}, instructions}) do
    walk({world, start}, instructions, fn pos, facing, bounds ->
      get_new_position_on_plane(pos, facing, bounds)
    end)
  end

  defp walk_cube({{world, start}, instructions}, test) do
    size = get_cube_size(world)
    mappings = if test, do: @test_mappings, else: @input_mappings

    walk({world, start}, instructions, fn pos, facing, bounds ->
      get_new_position_on_cube(pos, facing, bounds, size, mappings)
    end)
  end

  defp walk({world, start}, instructions, walk_fn) do
    instructions
    |> Enum.reduce({start, :right}, fn instruction, {pos, facing} ->
      case instruction do
        :right -> turn(pos, facing, :right)
        :left -> turn(pos, facing, :left)
        distance -> walk(pos, facing, distance, world, walk_fn)
      end
    end)
  end

  defp walk(pos, facing, distance, world, walk_fn) do
    distance..1
    |> Enum.reduce_while({pos, facing}, fn _, {pos, facing} ->
      bounds = get_boundaries(pos, world)
      {new_pos, new_facing} = walk_fn.(pos, facing, bounds)

      case Map.get(world, new_pos) do
        :wall -> {:halt, {pos, facing}}
        :tile -> {:cont, {new_pos, new_facing}}
        nil -> {:halt, :error}
      end
    end)
  end

  defp get_new_position_on_plane({x, y}, facing, bounds) do
    new_pos = move_in_direction({x, y}, facing)

    if out_of_bounds(new_pos, facing, bounds) do
      {wrap_around(new_pos, facing, bounds), facing}
    else
      {new_pos, facing}
    end
  end

  defp wrap_around({new_x, new_y}, facing, bounds) do
    {{min_x, max_x}, {min_y, max_y}} = bounds

    case facing do
      :right -> {min_x, new_y}
      :left -> {max_x, new_y}
      :down -> {new_x, min_y}
      :up -> {new_x, max_y}
    end
  end

  defp get_cube_size(world) do
    tiles_per_face = floor(map_size(world) / 6)
    floor(:math.sqrt(tiles_per_face))
  end

  defp get_cube_face({x, y}, cube_size) do
    {div(x, cube_size), div(y, cube_size)}
  end

  defp turn(pos, current, direction) do
    case direction do
      :right ->
        case current do
          :right -> {pos, :down}
          :down -> {pos, :left}
          :left -> {pos, :up}
          :up -> {pos, :right}
        end

      :left ->
        case current do
          :right -> {pos, :up}
          :down -> {pos, :right}
          :left -> {pos, :down}
          :up -> {pos, :left}
        end
    end
  end

  defp get_new_position_on_cube(pos, facing, bounds, cube_size, cube_mappings) do
    {next_x, next_y} = move_in_direction(pos, facing)

    cond do
      out_of_bounds({next_x, next_y}, facing, bounds) ->
        teleport(pos, facing, cube_size, cube_mappings)

      true ->
        {{next_x, next_y}, facing}
    end
  end

  defp move_in_direction({x, y}, facing) do
    case facing do
      :right -> {x + 1, y}
      :left -> {x - 1, y}
      :up -> {x, y - 1}
      :down -> {x, y + 1}
    end
  end

  defp out_of_bounds({x, y}, facing, bounds) do
    {{min_x, max_x}, {min_y, max_y}} = bounds

    case facing do
      :right -> x > max_x
      :left -> x < min_x
      :up -> y < min_y
      :down -> y > max_y
    end
  end

  defp teleport(pos, facing, cube_size, cube_mappings) do
    current_cube_face = get_cube_face(pos, cube_size)
    mapping = Map.get(cube_mappings, current_cube_face)
    {new_facing, new_face} = Map.get(mapping, facing)

    new_pos =
      pos
      |> reset(facing, cube_size)
      |> translate(new_face, cube_size)
      |> rotate(facing, new_facing, cube_size)

    {new_pos, new_facing}
  end

  defp reset(pos, facing, cube_size) do
    {x, y} = pos
    {current_face_x, current_face_y} = get_cube_face(pos, cube_size)
    {anchor_x, anchor_y} = {current_face_x * cube_size, current_face_y * cube_size}

    case facing do
      :up -> {x, anchor_y + cube_size - 1}
      :right -> {anchor_x, y}
      :down -> {x, anchor_y}
      :left -> {anchor_x + cube_size - 1, y}
    end
  end

  defp translate(pos, new_face, cube_size) do
    {x, y} = pos
    {current_face_x, current_face_y} = get_cube_face(pos, cube_size)

    {current_anchor_x, current_anchor_y} =
      {current_face_x * cube_size, current_face_y * cube_size}

    {offset_x, offset_y} = {x - current_anchor_x, y - current_anchor_y}

    {new_face_x, new_face_y} = new_face
    {new_anchor_x, new_anchor_y} = {new_face_x * cube_size, new_face_y * cube_size}
    {new_anchor_x + offset_x, new_anchor_y + offset_y}
  end

  defp rotate(pos, facing, new_facing, cube_size) do
    case {facing, new_facing} do
      {:up, :up} -> pos
      {:up, :right} -> rotate_clockwise(pos, 1, cube_size)
      {:up, :down} -> rotate_clockwise(pos, 2, cube_size)
      {:up, :left} -> rotate_clockwise(pos, 3, cube_size)
      {:right, :up} -> rotate_clockwise(pos, 3, cube_size)
      {:right, :right} -> pos
      {:right, :down} -> rotate_clockwise(pos, 1, cube_size)
      {:right, :left} -> rotate_clockwise(pos, 2, cube_size)
      {:down, :up} -> rotate_clockwise(pos, 2, cube_size)
      {:down, :right} -> rotate_clockwise(pos, 3, cube_size)
      {:down, :down} -> pos
      {:down, :left} -> rotate_clockwise(pos, 1, cube_size)
      {:left, :up} -> rotate_clockwise(pos, 1, cube_size)
      {:left, :right} -> rotate_clockwise(pos, 2, cube_size)
      {:left, :down} -> rotate_clockwise(pos, 3, cube_size)
      {:left, :left} -> pos
    end
  end

  defp rotate_clockwise(pos, times, cube_size) do
    cube_face = get_cube_face(pos, cube_size)
    {cube_face_x, cube_face_y} = cube_face
    {anchor_x, anchor_y} = {cube_face_x * cube_size, cube_face_y * cube_size}

    1..times
    |> Enum.reduce(pos, fn _, {x, y} ->
      {offset_x, offset_y} = {x - anchor_x, y - anchor_y}
      {anchor_x + (cube_size - 1 - offset_y), anchor_y + offset_x}
    end)
  end

  defp get_boundaries(pos, world) do
    {get_row_boundaries(pos, world), get_column_boundaries(pos, world)}
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
