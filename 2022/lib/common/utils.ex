defmodule AoC.Common do
  def split_lines(content) do
    String.split(content, ~r/\r\n|\n|\r/)
  end

  def split_double_lines(content) do
    String.split(content, ~r/((\r\n){2}|(\n){2}|(\r){2})/)
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
end