defmodule AoC.Days.D17 do
  @chamber_width 7
  @horizontal MapSet.new([{0, 0}, {1, 0}, {2, 0}, {3, 0}])
  @cross MapSet.new([{1, 0}, {0, 1}, {1, 1}, {2, 1}, {1, 2}])
  @comma MapSet.new([{0, 0}, {1, 0}, {2, 0}, {2, 1}, {2, 2}])
  @vertical MapSet.new([{0, 0}, {0, 1}, {0, 2}, {0, 3}])
  @block MapSet.new([{0, 0}, {1, 0}, {0, 1}, {1, 1}])
  @rock_order [@horizontal, @cross, @comma, @vertical, @block]

  def part_one(content) do
    {_, max_y} = parse_jet_stream(content) |> simulate(2022)
    height = max_y + 1
    result = height

    {:ok, result}
  end

  def part_two(content) do
    {_, max_y} = parse_jet_stream(content) |> simulate(1_000_000_000_000)
    height = max_y + 1
    result = height

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

  defp simulate(jet_stream, total_rocks) do
    {chamber, _, _, max_y} =
      0..(total_rocks - 1)
      |> Enum.reduce_while({MapSet.new(), 0, %{}, -1}, fn idx, {chamber, tick, seen, max_y} ->
        key =
          {rem(idx, length(@rock_order)), rem(tick, length(jet_stream)),
           make_surface_profile(chamber)}

        cond do
          Map.has_key?(seen, key) ->
            {previous_idx, previous_max_y} = Map.get(seen, key)
            repeats_every = idx - previous_idx
            max_y_increase = max_y - previous_max_y

            offset_total_rocks = total_rocks - previous_idx
            multiplier = div(offset_total_rocks, repeats_every)
            new_starting_index = multiplier * repeats_every + previous_idx
            new_max_y = multiplier * max_y_increase + previous_max_y

            max_y_diff = new_max_y - max_y

            new_chamber =
              chamber
              |> MapSet.to_list()
              |> Enum.reduce(MapSet.new(), fn {x, y}, chamber ->
                MapSet.put(chamber, {x, y + max_y_diff})
              end)

            {chamber, tick, max_y} =
              new_starting_index..(total_rocks - 1)
              |> Enum.reduce({new_chamber, tick, new_max_y}, fn idx, {chamber, tick, max_y} ->
                {chamber, tick, max_y} = simulate_rock(chamber, jet_stream, idx, tick, max_y)
                {chamber, tick, max_y}
              end)

            {:halt, {chamber, tick, MapSet.new(), max_y}}

          true ->
            seen = Map.put(seen, key, {idx, max_y})
            {chamber, tick, max_y} = simulate_rock(chamber, jet_stream, idx, tick, max_y)
            {:cont, {chamber, tick, seen, max_y}}
        end
      end)

    {chamber, max_y}
  end

  defp simulate_rock(chamber, jet_stream, idx, tick, current_max_y) do
    rock = get_rock(idx) |> place_rock(current_max_y)
    {chamber, tick, max_y} = simulate_rock_falling(chamber, jet_stream, rock, tick, current_max_y)

    {chamber, tick, max_y}
  end

  defp simulate_rock_falling(chamber, jet_stream, rock, tick, current_max_y) do
    {_, rock} = push_rock(chamber, jet_stream, rock, tick)

    case apply_gravity_to_rock(chamber, rock) do
      {:rest, rock} -> {MapSet.union(chamber, rock), tick + 1, max(current_max_y, max_y(rock))}
      {:fall, rock} -> simulate_rock_falling(chamber, jet_stream, rock, tick + 1, current_max_y)
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

  defp push_rock(chamber, jet_stream, rock, tick) do
    delta =
      case get_direction(jet_stream, tick) do
        :left -> -1
        :right -> 1
      end

    new_rock =
      rock |> MapSet.to_list() |> Enum.map(fn {x, y} -> {x + delta, y} end) |> MapSet.new()

    cond do
      intersects(chamber, new_rock) -> {:blocked, rock}
      !rock_within_horizontal_bounds(new_rock) -> {:blocked, rock}
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

  defp rock_within_horizontal_bounds(rock) do
    rock |> MapSet.to_list() |> Enum.all?(fn {x, _} -> x >= 0 and x < @chamber_width end)
  end

  defp rock_within_vertical_bounds(rock) do
    rock |> MapSet.to_list() |> Enum.all?(fn {_, y} -> y >= 0 end)
  end

  defp make_surface_profile(chamber) do
    case MapSet.size(chamber) do
      0 ->
        0..(@chamber_width - 1) |> Enum.map(fn _ -> 0 end)

      _ ->
        absolute_profile =
          chamber
          |> Enum.to_list()
          |> Enum.reduce(%{}, fn {x, y}, profile ->
            Map.put(profile, x, max(y, Map.get(profile, x, 0)))
          end)

        delta = Map.values(absolute_profile) |> Enum.min()

        0..(@chamber_width - 1) |> Enum.map(fn x -> Map.get(absolute_profile, x, -1) - delta end)
    end
  end

  # defp _draw_chamber(chamber, falling_rock) do
  #   max_y =
  #     MapSet.to_list(MapSet.union(chamber, falling_rock))
  #     |> Enum.reduce(0, fn {_, y}, max -> max(max, y) end)
  #
  #   output =
  #     max_y..0
  #     |> Enum.reduce("", fn y, output ->
  #       line =
  #         0..6
  #         |> Enum.reduce("", fn x, line ->
  #           line <>
  #             cond do
  #               MapSet.member?(falling_rock, {x, y}) -> "@"
  #               MapSet.member?(chamber, {x, y}) -> "#"
  #               true -> "."
  #             end
  #         end)
  #
  #       output <> "\n|#{line}|"
  #     end)
  #
  #   output = output <> "\n+" <> Enum.join(0..6 |> Enum.map(fn _ -> "-" end)) <> "+"
  #
  #   IO.puts(output)
  # end
end
