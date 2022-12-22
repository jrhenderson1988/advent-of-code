defmodule AoC.Days.D11 do
  @pattern ~r/Monkey (\d):\n  Starting items: (.+)\n  Operation: (.+)\n  Test: (.+)\n    If true: (.+)\n    If false: (.+)/
  @throw_to_monkey_pattern ~r/throw to monkey (\d+)/
  @divisible_by_pattern ~r/divisible by (\d+)/
  @operation_pattern ~r/new = (old|\d+) (\+|\*) (old|\d+)/

  def part_one(content) do
    {:ok, monkeys} = AoC.Common.normalize_lines(content) |> parse()
    result = calculate_monkey_business_after_rounds(monkeys, 20, fn wl, _, _ -> floor(wl / 3) end)

    {:ok, result}
  end

  def part_two(content) do
    {:ok, monkeys} = AoC.Common.normalize_lines(content) |> parse()

    result =
      calculate_monkey_business_after_rounds(monkeys, 10000, fn wl, _, monkeys ->
        divisor =
          monkeys |> Map.values() |> Enum.map(fn {_, _, test, _, _} -> test end) |> Enum.product()

        rem(wl, divisor)
      end)

    {:ok, result}
  end

  defp parse(content) do
    AoC.Common.split_double_lines(content)
    |> Enum.reduce({:ok, %{}}, fn chunk, acc ->
      case acc do
        {:ok, monkeys} ->
          {:ok, id, monkey} = parse_monkey(chunk)
          {:ok, Map.put(monkeys, id, monkey)}

        {:error, error} ->
          {:error, error}
      end
    end)
  end

  defp parse_monkey(content) do
    case Regex.run(@pattern, content) do
      [_, id, si, op, t, tt, tf] ->
        {id, _} = Integer.parse(id)

        monkey =
          {parse_starting_items(si), parse_operation(op), parse_test(t), parse_if_true(tt),
           parse_if_false(tf)}

        {:ok, id, monkey}
    end
  end

  defp parse_starting_items(content) do
    content
    |> AoC.Common.split_by_space()
    |> Enum.map(fn item ->
      {item, _} = Integer.parse(item)
      item
    end)
  end

  defp parse_operation(content) do
    case Regex.run(@operation_pattern, content) do
      [_, a, op, b] ->
        [a, b] =
          [a, b]
          |> Enum.map(fn x ->
            case x do
              "old" ->
                :old

              _ ->
                {num, _} = Integer.parse(x)
                num
            end
          end)

        op =
          case op do
            "*" -> :mul
            "+" -> :add
          end

        {a, b, op}
    end
  end

  defp parse_test(content) do
    parse_int_with_regex(@divisible_by_pattern, content)
  end

  defp parse_if_true(content) do
    parse_int_with_regex(@throw_to_monkey_pattern, content)
  end

  defp parse_if_false(content) do
    parse_int_with_regex(@throw_to_monkey_pattern, content)
  end

  defp parse_int_with_regex(regex, content) do
    case Regex.run(regex, content) do
      [_, parsed] ->
        {value, _} = Integer.parse(parsed)
        value
    end
  end

  defp calculate_monkey_business_after_rounds(monkeys, rounds, worry_level_adjuster) do
    totals = Map.keys(monkeys) |> Enum.reduce(%{}, fn id, acc -> Map.put(acc, id, 0) end)

    {totals, _} =
      0..(rounds - 1)
      |> Enum.reduce({totals, monkeys}, fn _, {totals, monkeys} ->
        play_round(totals, monkeys, worry_level_adjuster)
      end)

    calculate_monkey_business(totals)
  end

  defp play_round(totals, monkeys, worry_level_adjuster) do
    {totals, monkeys} =
      Map.keys(monkeys)
      |> Enum.reduce({totals, monkeys}, fn id, {totals, monkeys} ->
        totals =
          Map.put(totals, id, Map.get(totals, id) + total_items_held_by_monkey(monkeys, id))

        monkeys = monkey_take_turn(monkeys, id, worry_level_adjuster)

        {totals, monkeys}
      end)

    {totals, monkeys}
  end

  defp calculate_monkey_business(totals) do
    Enum.map(totals, fn {_, total} -> total end)
    |> Enum.sort(:desc)
    |> Enum.take(2)
    |> Enum.product()
  end

  defp get_worry_level({a, b, op}, old) do
    first = if a == :old, do: old, else: a
    second = if b == :old, do: old, else: b

    case op do
      :mul -> first * second
      :add -> first + second
    end
  end

  defp get_target_monkey(test, if_true, if_false, item) do
    cond do
      rem(item, test) == 0 -> if_true
      true -> if_false
    end
  end

  defp monkey_take_turn(monkeys, id, worry_level_adjuster) do
    monkey = Map.get(monkeys, id)
    {items, op, test, if_true, if_false} = monkey

    items
    |> Enum.reduce(monkeys, fn item, monkeys ->
      new_worry_level = worry_level_adjuster.(get_worry_level(op, item), test, monkeys)
      target_monkey = get_target_monkey(test, if_true, if_false, new_worry_level)

      {tm_items, tm_op, tm_test, tm_if_true, tm_if_false} = Map.get(monkeys, target_monkey)

      Map.put(
        monkeys,
        target_monkey,
        {tm_items ++ [new_worry_level], tm_op, tm_test, tm_if_true, tm_if_false}
      )
    end)
    |> Map.put(id, {[], op, test, if_true, if_false})
  end

  defp total_items_held_by_monkey(monkeys, id) do
    {items, _, _, _, _} = Map.get(monkeys, id)
    length(items)
  end
end
