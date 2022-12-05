defmodule AoC.Days.D05 do
  def part_one(content) do
    {:ok, columns, stacks, instructions} = parse(content)

    stacks = apply_instructions(stacks, instructions, &one_crate_at_a_time/2)

    result = get_output(columns, stacks)

    {:ok, result}
  end

  def part_two(content) do
    {:ok, columns, stacks, instructions} = parse(content)

    stacks = apply_instructions(stacks, instructions, &multiple_crates_at_a_time/2)

    result = get_output(columns, stacks)

    {:ok, result}
  end

  defp parse(content) do
    [stacks_content, instructions_content] = AoC.Common.split_double_lines(content)
    {:ok, columns, stacks} = parse_stacks(stacks_content)
    {:ok, instructions} = parse_instructions(instructions_content)

    {:ok, columns, stacks, instructions}
  end

  defp parse_stacks(content) do
    lines = AoC.Common.split_lines(content)

    columns = parse_columns(List.last(lines))

    stacks =
      lines
      |> Enum.reverse()
      |> Enum.drop(1)
      |> Enum.reduce(create_initial_stacks(columns), fn line, stacks ->
        columns
        |> Enum.with_index()
        |> Enum.map(fn {column, index} -> parse_crate(line, column, index) end)
        |> Enum.filter(fn {crate, _} -> crate != "" end)
        |> Enum.reduce(stacks, fn {crate, column}, stacks ->
          add_crate_to_stacks(stacks, column, crate)
        end)
      end)

    {:ok, columns, stacks}
  end

  defp parse_columns(line) do
    line
    |> String.codepoints()
    |> Enum.chunk_every(4)
    |> Enum.map(&Enum.join/1)
    |> Enum.map(&String.trim/1)
  end

  defp create_initial_stacks(columns) do
    Enum.reduce(columns, %{}, fn column, acc -> Map.put(acc, column, []) end)
  end

  defp add_crate_to_stacks(stacks, column, crate) do
    existing = Map.get(stacks, column)
    updated = existing ++ [crate]
    Map.put(stacks, column, updated)
  end

  defp parse_crate(line, column, index) do
    crate =
      String.slice(line, (index * 4)..(index * 4 + 3))
      |> String.trim()
      |> String.trim("[")
      |> String.trim("]")

    {crate, column}
  end

  defp parse_instructions(instructions_content) do
    instructions =
      AoC.Common.split_lines(instructions_content)
      |> Enum.map(fn line ->
        %{"amount" => amount, "source" => source, "target" => target} =
          Regex.named_captures(
            ~r/move (?<amount>\d+) from (?<source>\d+) to (?<target>\d+)/,
            line
          )

        {amount, _} = Integer.parse(amount)
        {amount, source, target}
      end)

    {:ok, instructions}
  end

  defp get_output(columns, stacks) do
    columns
    |> Enum.map(fn column -> Map.get(stacks, column) end)
    |> Enum.map(fn stack -> List.last(stack) end)
    |> Enum.join()
  end

  defp apply_instructions(stacks, instructions, func) do
    Enum.reduce(instructions, stacks, fn instruction, stacks ->
      func.(stacks, instruction)
    end)
  end

  defp one_crate_at_a_time(stacks, {amount, source, target}) do
    Range.new(0, amount - 1)
    |> Enum.reduce(stacks, fn _, stacks ->
      source_stack = Map.get(stacks, source)
      target_stack = Map.get(stacks, target)

      stacks = Map.put(stacks, target, target_stack ++ [List.last(source_stack)])
      stacks = Map.put(stacks, source, Enum.drop(source_stack, -1))

      stacks
    end)
  end

  defp multiple_crates_at_a_time(stacks, {amount, source, target}) do
    source_stack = Map.get(stacks, source)
    target_stack = Map.get(stacks, target)

    stacks = Map.put(stacks, target, target_stack ++ Enum.slice(source_stack, -amount..-1))
    stacks = Map.put(stacks, source, Enum.slice(source_stack, 0..-(amount + 1)))

    stacks
  end
end
