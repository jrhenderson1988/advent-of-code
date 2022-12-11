defmodule AoC.Days.D08 do
  def part_one(content) do
    {:ok, forest} = parse_forest(content)
    result = total_visible_trees(forest)

    {:ok, result}
  end

  def part_two(content) do
    {:ok, forest} = parse_forest(content)
    result = calculate_highest_scenic_score(forest)

    {:ok, result}
  end

  defp parse_forest(content) do
    forest =
      AoC.Common.split_lines(content)
      |> Enum.map(&parse_row/1)

    {:ok, forest}
  end

  defp parse_row(line) do
    String.to_charlist(line)
    |> Enum.map(fn ch -> ch - 48 end)
  end

  defp total_visible_trees(forest) do
    total_visible_interior_trees(forest) + total_visible_exterior_trees(forest)
  end

  defp total_visible_interior_trees(forest) do
    AoC.Common.combine(1..(height(forest) - 2), 1..(width(forest) - 2))
    |> Enum.reduce(0, fn {row, col}, total ->
      cond do
        is_visible_from_edge(forest, row, col) -> total + 1
        true -> total
      end
    end)
  end

  defp total_visible_exterior_trees(forest) do
    height(forest) * 2 + (width(forest) - 2) * 2
  end

  defp tree_at(forest, row_index, col_index) do
    Enum.at(forest, row_index) |> Enum.at(col_index)
  end

  defp is_visible_from_edge(forest, row_index, col_index) do
    [&look_left/3, &look_right/3, &look_up/3, &look_down/3]
    |> Enum.any?(fn look_func ->
      visible_from_edge(forest, row_index, col_index, look_func)
    end)
  end

  defp look_left(_forest, row, col), do: (col - 1)..0 |> Enum.map(fn col -> {row, col} end)

  defp look_right(forest, row, col),
    do: (col + 1)..(width(forest) - 1) |> Enum.map(fn col -> {row, col} end)

  defp look_up(_forest, row, col), do: (row - 1)..0 |> Enum.map(fn row -> {row, col} end)

  defp look_down(forest, row, col),
    do: (row + 1)..(height(forest) - 1) |> Enum.map(fn row -> {row, col} end)

  defp tree_at_shorter_than(forest, row, col, current), do: tree_at(forest, row, col) < current

  defp visible_from_edge(forest, row, col, look_func) do
    tree = tree_at(forest, row, col)

    look_func.(forest, row, col)
    |> Enum.reduce(true, fn {row, col}, visible ->
      visible and tree_at_shorter_than(forest, row, col, tree)
    end)
  end

  defp width(forest) do
    Enum.at(forest, 0) |> length()
  end

  defp height(forest) do
    length(forest)
  end

  defp calculate_highest_scenic_score(forest) do
    AoC.Common.combine(1..(height(forest) - 2), 1..(width(forest) - 2))
    |> Enum.reduce(0, fn tree, high_score ->
      ss = scenic_score_for(forest, tree)

      cond do
        ss > high_score -> ss
        true -> high_score
      end
    end)
  end

  defp scenic_score_for(forest, tree_position) do
    [&look_left/3, &look_right/3, &look_up/3, &look_down/3]
    |> Enum.map(fn look_func ->
      calculate_viewing_distance(forest, tree_position, look_func)
    end)
    |> Enum.product()
  end

  defp calculate_viewing_distance(forest, {row, col}, look_func) do
    tree = tree_at(forest, row, col)

    {_, distance} =
      look_func.(forest, row, col)
      |> Enum.reduce({:go, 0}, fn {other_row, other_col}, acc ->
        case acc do
          {:go, dist} ->
            other = tree_at(forest, other_row, other_col)

            cond do
              other >= tree -> {:stop, dist + 1}
              true -> {:go, dist + 1}
            end

          {:stop, dist} ->
            {:stop, dist}
        end
      end)

    distance
  end
end
