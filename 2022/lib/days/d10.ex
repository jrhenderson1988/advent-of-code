defmodule AoC.Days.D10 do
  def part_one(content) do
    result =
      parse_instructions(content)
      |> sum_signal_strengths_for_cycles(1, [20, 60, 100, 140, 180, 220])

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp parse_instructions(content) do
    AoC.Common.split_lines(content)
    |> Enum.map(&parse_line/1)
  end

  defp parse_line(line) do
    [cmd | args] = String.trim(line) |> String.split(" ")

    case cmd do
      "noop" ->
        {:noop}

      "addx" ->
        [arg | _] = args
        {value, _} = Integer.parse(arg)
        {:addx, value}
    end
  end

  defp sum_signal_strengths_for_cycles(instructions, initial_register_value, cycle_limits) do
    get_signal_strengths_at_cycles(instructions, initial_register_value, cycle_limits)
    |> Enum.sum()
  end

  defp get_signal_strengths_at_cycles(instructions, initial_register_value, cycle_limits) do
    Enum.map(cycle_limits, fn cycle_limit ->
      {register_value, cycle_number} =
        execute_until_cycle_limit(initial_register_value, instructions, cycle_limit)

      register_value * cycle_number
    end)
  end

  defp execute_until_cycle_limit(initial_register_value, instructions, cycle_limit) do
    {register_value, cycle_number} =
      instructions
      |> Enum.reduce({initial_register_value, 1}, fn instruction, {register, cycle_number} ->
        if cycle_number < cycle_limit do
          stages = apply_instruction(instruction, {register, cycle_number})

          update =
            stages
            |> Enum.find(
              Enum.at(stages, -1),
              fn {_, cycle_number} -> cycle_number == cycle_limit end
            )

          update
        else
          {register, cycle_number}
        end
      end)

    {register_value, cycle_number}
  end

  defp apply_instruction(instruction, {register, cycle_number}) do
    case instruction do
      {:noop} -> [{register, cycle_number + 1}]
      {:addx, amount} -> [{register, cycle_number + 1}, {register + amount, cycle_number + 2}]
    end
  end
end
