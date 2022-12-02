defmodule AoC do
  def main(args) do
    with {:ok, day, part} <- parse_args(args),
         {:ok, day} <- to_day(day),
         {:ok, part} <- to_part(part) do
      execute(day, part)
    else
      {:error, err} -> IO.puts("Invalid arguments: #{inspect(err)}")
    end
  end

  defp parse_args(args) do
    case length(args) do
      1 ->
        [day] = args
        {:ok, day, nil}

      2 ->
        [day, part] = args
        {:ok, day, part}

      _ ->
        {:error, :invalid_arguments}
    end
  end

  defp to_day(arg) do
    case Integer.parse(arg) do
      {i, _} ->
        cond do
          i < 1 -> {:error, :invalid_day}
          i > 25 -> {:error, :invalid_day}
          true -> {:ok, i}
        end

      :error ->
        {:error, :invalid_day}
    end
  end

  defp to_part(nil), do: {:ok, :both}

  defp to_part(arg) do
    case Integer.parse(arg) do
      {i, _} ->
        case i do
          1 -> {:ok, :one}
          2 -> {:ok, :two}
          _ -> {:error, :invalid_part}
        end

      _ ->
        {:error, :invalid_part}
    end
  end

  defp execute(day, part) do
    with {:ok, module} <- get_module_for_day(day),
         {:ok, part_functions} <- get_part_functions(part) do
      Enum.each(part_functions, fn part_function -> evaluate(module, part_function, []) end)
    else
      {:error, err} -> IO.puts(:stderr, err)
    end
  end

  defp get_module_for_day(day) do
    try do
      name = "Elixir.AoC.Days.D#{String.pad_leading("#{day}", 2, "0")}"
      {:ok, String.to_existing_atom(name)}
    rescue
      ArgumentError -> {:error, :day_not_yet_implemented}
    end
  end

  defp get_part_functions(part) do
    case part do
      :one -> {:ok, [:part_one]}
      :two -> {:ok, [:part_two]}
      :both -> {:ok, [:part_one, :part_two]}
    end
  end

  defp evaluate(module, func, args) do
    {time, result} = time(fn -> apply(module, func, args) end)

    case result do
      {:ok, answer} ->
        IO.puts("==============================")
        IO.puts("#{func}: #{answer}")

      {:error, error} ->
        IO.puts("##############################")
        IO.puts("ERROR: #{func}: #{error}")
    end

    IO.puts("Time taken: #{time}ms")
  end

  defp time(func) do
    {time, result} = :timer.tc(func)
    {time, result}
  end
end
