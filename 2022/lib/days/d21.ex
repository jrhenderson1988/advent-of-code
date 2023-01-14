defmodule AoC.Days.D21 do
  def part_one(content) do
    result = parse(content) |> evaluate("root") |> floor()

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp evaluate(monkeys, name) do
    case Map.get(monkeys, name) do
      nil ->
        :error

      {:lit, num} ->
        num

      {op, a, b} ->
        case op do
          :sub -> evaluate(monkeys, a) - evaluate(monkeys, b)
          :add -> evaluate(monkeys, a) + evaluate(monkeys, b)
          :div -> evaluate(monkeys, a) / evaluate(monkeys, b)
          :mul -> evaluate(monkeys, a) * evaluate(monkeys, b)
        end
    end
  end

  defp parse(content) do
    AoC.Common.split_lines(content) |> Enum.reduce(%{}, &parse_monkey/2)
  end

  defp parse_monkey(line, monkeys) do
    [name, op] = String.trim(line) |> String.split(":") |> Enum.map(&String.trim/1)

    Map.put(monkeys, name, parse_op(op))
  end

  defp parse_op(s) do
    op =
      [{"-", :sub}, {"+", :add}, {"/", :div}, {"*", :mul}]
      |> Enum.reduce(nil, fn {op_s, op}, match ->
        cond do
          match == nil and String.contains?(s, op_s) ->
            [a, b] = String.split(s, op_s) |> Enum.map(&String.trim/1)
            {op, a, b}

          true ->
            match
        end
      end)

    cond do
      op == nil ->
        {v, _} = String.trim(s) |> Integer.parse()
        {:lit, v}

      true ->
        op
    end
  end
end
