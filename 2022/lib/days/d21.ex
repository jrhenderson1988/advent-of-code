defmodule AoC.Days.D21 do
  def part_one(content) do
    result = parse(content) |> evaluate("root") |> floor()

    {:ok, result}
  end

  def part_two(content) do
    result = parse(content) |> find_humn_number()

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

  defp find_humn_number(monkeys) do
    {_, lhs, rhs} = Map.get(monkeys, "root")

    cond do
      depends_on_humn(monkeys, lhs) -> evaluate_humn(monkeys, lhs, evaluate(monkeys, rhs))
      depends_on_humn(monkeys, rhs) -> evaluate_humn(monkeys, rhs, evaluate(monkeys, lhs))
      true -> :error
    end
  end

  defp depends_on_humn(monkeys, name) do
    case name do
      "humn" ->
        true

      name ->
        case Map.get(monkeys, name) do
          {:lit, _} ->
            false

          {_, a, b} ->
            depends_on_humn(monkeys, a) or depends_on_humn(monkeys, b)
        end
    end
  end

  defp evaluate_humn(monkeys, to_evaluate, literal) do
    equation = build_equation(monkeys, to_evaluate)
    solve_equation(equation, literal)
  end

  defp build_equation(monkeys, name) do
    case name do
      "humn" ->
        :humn

      name ->
        case Map.get(monkeys, name) do
          {:lit, n} ->
            n

          {op, a, b} ->
            {op, build_equation(monkeys, a), build_equation(monkeys, b)}
        end
    end
  end

  defp solve_equation(equation, value) do
    case equation do
      {op, a, b} when is_number(a) and is_number(b) -> evaluate_literals(op, a, b)
      {:div, a, b} when is_number(b) -> solve_equation(a, value * b)
      {:div, a, b} when is_number(a) -> solve_equation(b, a / value)
      {:mul, a, b} when is_number(b) -> solve_equation(a, floor(value / b))
      {:mul, a, b} when is_number(a) -> solve_equation(b, floor(value / a))
      {:sub, a, b} when is_number(b) -> solve_equation(a, value + b)
      {:sub, a, b} when is_number(a) -> solve_equation(b, a - value)
      {:add, a, b} when is_number(b) -> solve_equation(a, value - b)
      {:add, a, b} when is_number(a) -> solve_equation(b, value - a)
      {op, a, b} -> solve_equation({op, simplify(a), simplify(b)}, value)
      :humn -> value
      a when is_number(a) -> a
    end
  end

  defp simplify(eq) do
    cond do
      contains_humn(eq) ->
        eq

      true ->
        case eq do
          eq when is_integer(eq) ->
            eq

          eq when is_float(eq) ->
            floor(eq)

          {op, a, b} when is_number(a) and is_number(b) ->
            evaluate_literals(op, a, b)

          {op, a, b} ->
            a = simplify(a)
            b = simplify(b)

            cond do
              is_number(a) and is_number(b) -> evaluate_literals(op, a, b)
              true -> {op, a, b}
            end
        end
    end
  end

  defp contains_humn(eq) do
    case eq do
      :humn -> true
      v when is_number(v) -> false
      {_, a, b} -> contains_humn(a) or contains_humn(b)
    end
  end

  defp evaluate_literals(op, a, b) do
    case op do
      :add -> a + b
      :sub -> a - b
      :div -> floor(a / b)
      :mul -> a * b
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
