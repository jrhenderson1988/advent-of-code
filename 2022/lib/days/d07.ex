defmodule AoC.Days.D07 do
  @total_disk_space 70_000_000
  @minimum_unused_space_required 30_000_000

  def part_one(content) do
    {:ok, structure} = parse_directory_structure(content)
    result = find_sum_of_directories_larger_than_1000(structure)

    {:ok, result}
  end

  def part_two(content) do
    {:ok, structure} = parse_directory_structure(content)
    result = size_of_chosen_directory_to_delete(structure)

    {:ok, result}
  end

  defp parse_directory_structure(content) do
    {_, structure} =
      AoC.Common.split_lines(content)
      |> Enum.reduce({[], %{}}, fn line, {pwd, structure} ->
        case parse_line(line) do
          {:command, :ls} ->
            {pwd, structure}

          {:command, :cd, path} ->
            case path do
              ".." -> {Enum.drop(pwd, -1), structure}
              "/" -> {[], structure}
              other -> {pwd ++ [other], structure}
            end

          {:entry, :dir, name} ->
            {pwd, put_in_structure(structure, pwd ++ [name], %{})}

          {:entry, :file, size, name} ->
            {pwd, put_in_structure(structure, pwd ++ [name], size)}
        end
      end)

    {:ok, structure}
  end

  defp parse_line(line) do
    trimmed = String.trim(line)

    case String.starts_with?(trimmed, "$") do
      true -> parse_command(trimmed)
      false -> parse_entry(trimmed)
    end
  end

  defp parse_command(line) do
    cond do
      String.starts_with?(line, "$ ls") ->
        {:command, :ls}

      String.starts_with?(line, "$ cd") ->
        {:command, :cd, String.slice(line, 4..-1) |> String.trim()}

      true ->
        {:error, :invalid_command}
    end
  end

  defp parse_entry(line) do
    cond do
      String.starts_with?(line, "dir") ->
        {:entry, :dir, String.slice(line, 3..-1) |> String.trim()}

      true ->
        [size, filename] = String.split(line, " ")
        {size, _} = Integer.parse(size)
        {:entry, :file, size, filename}
    end
  end

  defp put_in_structure(structure, key, value) do
    case length(key) do
      0 ->
        {:error, :invalid_key}

      1 ->
        Map.put(structure, Enum.at(key, 0), value)

      _ ->
        [first | rest] = key
        sub_structure = Map.get(structure, first, %{})
        updated = put_in_structure(sub_structure, rest, value)
        Map.put(structure, first, updated)
    end
  end

  defp find_sum_of_directories_larger_than_1000(structure) do
    Enum.reduce(structure, 0, fn {_, v}, total ->
      cond do
        is_map(v) ->
          size = size_of_structure(v)

          total + find_sum_of_directories_larger_than_1000(v) +
            cond do
              size < 100_000 ->
                size

              true ->
                0
            end

        is_integer(v) ->
          total
      end
    end)
  end

  defp size_of_structure(structure) do
    Enum.reduce(structure, 0, fn {_, v}, acc ->
      acc +
        cond do
          is_integer(v) -> v
          is_map(v) -> size_of_structure(v)
        end
    end)
  end

  defp size_of_chosen_directory_to_delete(structure) do
    size_of_structure = size_of_structure(structure)
    total_current_size = @total_disk_space - size_of_structure
    free_space_needed = @minimum_unused_space_required - total_current_size
    size_of_chosen_directory_to_delete(structure, size_of_structure, free_space_needed)
  end

  defp size_of_chosen_directory_to_delete(structure, current_best, free_space_needed) do
    Enum.reduce(structure, current_best, fn {_, v}, current ->
      cond do
        is_map(v) ->
          size = size_of_structure(v)
          sub_size = size_of_chosen_directory_to_delete(v, current, free_space_needed)

          new_best =
            cond do
              size >= free_space_needed and size < current -> size
              true -> current
            end

          cond do
            sub_size < new_best -> sub_size
            true -> new_best
          end

        is_integer(v) ->
          current
      end
    end)
  end
end
