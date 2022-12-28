defmodule AoC.Common do
  def split_lines(content) do
    String.split(content, ~r/\r\n|\n|\r/)
  end

  def split_double_lines(content) do
    String.split(content, ~r/((\r\n){2}|(\n){2}|(\r){2})/)
  end

  def normalize_lines(content) do
    String.replace(content, ~r/\r\n|\r|\n/, "\n")
  end

  def split_by_space(content) do
    String.split(content, ~r/ +/)
  end

  def combine(first, second) do
    first
    |> Enum.reduce([], fn row, entries ->
      entries ++
        (second
         |> Enum.reduce([], fn col, entries ->
           entries ++ [{row, col}]
         end))
    end)
  end

  def characters(str) do
    String.to_charlist(str)
  end

  def manhattan_distance({ax, ay}, {bx, by}) do
    abs(ax - bx) + abs(ay - by)
  end

  # points should be a set
  defp draw_grid(points, show_zero_zero \\ false) do
    {{sx, sy}, {lx, ly}} =
      points
      |> Enum.reduce({nil, nil}, fn {x, y}, {smallest, largest} ->
        smallest =
          case smallest do
            nil -> {x, y}
            {sx, sy} -> {if(x < sx, do: x, else: sx), if(y < sy, do: y, else: sy)}
          end

        largest =
          case largest do
            nil -> {x, y}
            {lx, ly} -> {if(x > lx, do: x, else: lx), if(y > ly, do: y, else: ly)}
          end

        {smallest, largest}
      end)

    sx = if show_zero_zero and sx > 1, do: 1, else: sx
    sy = if show_zero_zero and sy > 1, do: 1, else: sy

    output =
      (ly + 1)..(sy - 1)
      |> Enum.reduce("\n", fn y, output ->
        line =
          (sx - 1)..(lx + 1)
          |> Enum.reduce("", fn x, line ->
            line <>
              cond do
                MapSet.member?(points, {x, y}) -> "#"
                {x, y} == {0, 0} -> "+"
                x == 0 -> "|"
                y == 0 -> "-"
                true -> "."
              end
          end)

        output <> "\n" <> line
      end)

    IO.puts(output)
  end
end
