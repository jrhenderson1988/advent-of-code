defmodule AoC.Days.D10 do
  def part_one(content) do
    coordinates = parse_instructions(content) |> calculate_register_at_each_cycle(1)

    result =
      [20, 60, 100, 140, 180, 220]
      |> Enum.map(fn at -> Enum.at(coordinates, at - 1) * at end)
      |> Enum.sum()

    {:ok, result}
  end

  def part_two(content) do
    result = parse_instructions(content) |> render_image(40)

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

  defp render_image(instructions, width) do
    coordinates = calculate_register_at_each_cycle(instructions, 1)

    pixels =
      0..239
      |> Enum.map(fn index ->
        sprite_pos = Enum.at(coordinates, index)
        sprite_range = (sprite_pos - 1)..(sprite_pos + 1)

        rem(index, width) in sprite_range
      end)

    draw_image(pixels, width)
  end

  defp draw_image(pixels, width) do
    pixels
    |> Enum.with_index()
    |> Enum.map(fn {pixel, i} ->
      "#{if rem(i, width) == 0, do: "\n", else: ""}#{if pixel, do: "#", else: "."}"
    end)
    |> Enum.join()
    |> String.trim()
  end

  defp calculate_register_at_each_cycle(instructions, register) do
    {_, coordinates} =
      instructions
      |> Enum.reduce({register, []}, fn instruction, {register, coordinates} ->
        case instruction do
          {:noop} -> {register, coordinates ++ [register]}
          {:addx, amount} -> {register + amount, coordinates ++ [register, register]}
        end
      end)

    coordinates
  end
end
