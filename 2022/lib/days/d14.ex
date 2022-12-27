defmodule AoC.Days.D14 do
  def part_one(content) do
    cave = parse(content)
    lowest_point = find_lowest_point(cave)
    filled_cave = simulate_sand_until_abyss(cave, lowest_point, {500, 0})
    result = MapSet.size(filled_cave) - MapSet.size(cave)

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp parse(content) do
    AoC.Common.split_lines(content)
    |> Enum.reduce(MapSet.new(), fn line, cave -> parse_line(line, cave) end)
  end

  defp parse_line(line, cave) do
    {cave, _} =
      String.trim(line)
      |> String.split("->")
      |> Enum.map(fn coord ->
        [x, y] =
          String.trim(coord)
          |> String.split(",")
          |> Enum.map(fn str ->
            {int, _} = Integer.parse(str)
            int
          end)

        {x, y}
      end)
      |> Enum.reduce({cave, nil}, fn curr, {cave, prev} ->
        case prev do
          nil ->
            {cave, curr}

          prev ->
            {px, py} = prev
            {cx, cy} = curr

            cave =
              px..cx
              |> Enum.reduce(cave, fn x, cave ->
                py..cy
                |> Enum.reduce(cave, fn y, cave ->
                  MapSet.put(cave, {x, y})
                end)
              end)

            {cave, curr}
        end
      end)

    cave
  end

  defp find_lowest_point(cave) do
    cave
    |> Enum.reduce(nil, fn {_, y}, lowest ->
      case lowest do
        nil -> y
        lowest -> if y > lowest, do: y, else: lowest
      end
    end)
  end

  defp simulate_sand_until_abyss(cave, lowest_point, fall_point) do
    case simulate_single_grain_of_sand(cave, lowest_point, fall_point) do
      {:halt, cave} -> cave
      {:continue, cave} -> simulate_sand_until_abyss(cave, lowest_point, fall_point)
    end
  end

  defp simulate_single_grain_of_sand(cave, lowest_point, {x, y}) do
    cond do
      y > lowest_point ->
        {:halt, cave}

      !MapSet.member?(cave, {x, y + 1}) ->
        simulate_single_grain_of_sand(cave, lowest_point, {x, y + 1})

      !MapSet.member?(cave, {x - 1, y + 1}) ->
        simulate_single_grain_of_sand(cave, lowest_point, {x - 1, y + 1})

      !MapSet.member?(cave, {x + 1, y + 1}) ->
        simulate_single_grain_of_sand(cave, lowest_point, {x + 1, y + 1})

      true ->
        {:continue, MapSet.put(cave, {x, y})}
    end
  end
end
