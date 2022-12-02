defmodule AoC.Common do
  def split_lines(content) do
    String.split(content, ~r/\r\n|\n|\r/)
  end

  def split_double_lines(content) do
    String.split(content, ~r/((\r\n){2}|(\n){2}|(\r){2})/)
  end
end
