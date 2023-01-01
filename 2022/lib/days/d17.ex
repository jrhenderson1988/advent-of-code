defmodule AoC.Days.D17 do
  @horizontal MapSet.new([{0, 0}, {1, 0}, {2, 0}, {3, 0}])
  @cross MapSet.new([{1, 0}, {0, 1}, {1, 1}, {2, 1}, {1, 2}])
  @comma MapSet.new([{0, 0}, {1, 0}, {2, 0}, {2, 1}, {2, 2}])
  @vertical MapSet.new([{0, 0}, {0, 1}, {0, 2}, {0, 3}])
  @block MapSet.new([{0, 0}, {1, 0}, {0, 1}, {1, 1}])
  @rock_order [@horizontal, @cross, @comma, @vertical, @block]

  def part_one(content) do
    highest_y = parse_jet_stream(content) |> simulate(7, 2022) |> max_y()
    height = highest_y + 1
    result = height

    {:ok, result}
  end

  def part_two(content) do
    result = -1

    {:ok, result}
  end

  defp parse_jet_stream(content) do
    String.trim(content)
    |> String.to_charlist()
    |> Enum.map(fn ch ->
      case ch do
        ?< -> :left
        ?> -> :right
      end
    end)
  end

  defp simulate(jet_stream, chamber_width, total_rocks) do
    {chamber, _} =
      0..(total_rocks - 1)
      |> Enum.reduce({MapSet.new(), 0}, fn rock_idx, {chamber, tick} ->
        {chamber, tick} = simulate_rock(chamber, jet_stream, chamber_width, rock_idx, tick)
        {chamber, tick}
      end)

    chamber
  end

  defp simulate_rock(chamber, jet_stream, chamber_width, rock_idx, tick) do
    current_max = max_y(chamber)
    rock = get_rock(rock_idx) |> place_rock(current_max)
    {chamber, tick} = simulate_rock_falling(chamber, jet_stream, chamber_width, rock, tick)
    {chamber, tick}
  end

  defp simulate_rock_falling(chamber, jet_stream, chamber_width, rock, tick) do
    {_, rock} = push_rock(chamber, jet_stream, chamber_width, rock, tick)

    case apply_gravity_to_rock(chamber, rock) do
      {:rest, rock} -> {MapSet.union(chamber, rock), tick + 1}
      {:fall, rock} -> simulate_rock_falling(chamber, jet_stream, chamber_width, rock, tick + 1)
    end
  end

  defp get_rock(iteration) do
    idx = rem(iteration, length(@rock_order))
    Enum.at(@rock_order, idx)
  end

  defp get_direction(jet_stream, tick) do
    idx = rem(tick, length(jet_stream))
    Enum.at(jet_stream, idx)
  end

  defp place_rock(rock, current_max) do
    MapSet.to_list(rock)
    |> Enum.map(fn {x, y} ->
      # need 3 spaces above current max
      {x + 2, y + current_max + 4}
    end)
    |> MapSet.new()
  end

  defp push_rock(chamber, jet_stream, chamber_width, rock, tick) do
    delta =
      case get_direction(jet_stream, tick) do
        :left -> -1
        :right -> 1
      end

    new_rock =
      rock |> MapSet.to_list() |> Enum.map(fn {x, y} -> {x + delta, y} end) |> MapSet.new()

    cond do
      intersects(chamber, new_rock) -> {:blocked, rock}
      !rock_within_horizontal_bounds(chamber_width, new_rock) -> {:blocked, rock}
      true -> {:pushed, new_rock}
    end
  end

  defp apply_gravity_to_rock(chamber, rock) do
    new_rock = rock |> MapSet.to_list() |> Enum.map(fn {x, y} -> {x, y - 1} end) |> MapSet.new()

    cond do
      intersects(chamber, new_rock) -> {:rest, rock}
      !rock_within_vertical_bounds(new_rock) -> {:rest, rock}
      true -> {:fall, new_rock}
    end
  end

  defp max_y(chamber) do
    chamber
    |> MapSet.to_list()
    |> Enum.reduce(-1, fn {_, y}, current_max -> max(current_max, y) end)
  end

  defp intersects(a, b) do
    !MapSet.disjoint?(a, b)
  end

  defp rock_within_horizontal_bounds(chamber_width, rock) do
    rock |> MapSet.to_list() |> Enum.all?(fn {x, _} -> x >= 0 and x < chamber_width end)
  end

  defp rock_within_vertical_bounds(rock) do
    rock |> MapSet.to_list() |> Enum.all?(fn {_, y} -> y >= 0 end)
  end

  defp draw_chamber(chamber, falling_rock) do
    max_y =
      MapSet.to_list(MapSet.union(chamber, falling_rock))
      |> Enum.reduce(0, fn {_, y}, max -> max(max, y) end)

    output =
      max_y..0
      |> Enum.reduce("", fn y, output ->
        line =
          0..6
          |> Enum.reduce("", fn x, line ->
            line <>
              cond do
                MapSet.member?(falling_rock, {x, y}) -> "@"
                MapSet.member?(chamber, {x, y}) -> "#"
                true -> "."
              end
          end)

        output <> "\n|#{line}|"
      end)

    output = output <> "\n+" <> Enum.join(0..6 |> Enum.map(fn _ -> "-" end)) <> "+"

    IO.puts(output)
  end
end
