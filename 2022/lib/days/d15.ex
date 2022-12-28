defmodule AoC.Days.D15 do
  @line_pattern ~r/Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)/

  def part_one(content, target_row \\ 2_000_000) do
    result = parse(content) |> count_non_beacons_positions_on_row(target_row)

    {:ok, result}
  end

  def part_two(content, max_coord \\ 4_000_000) do

    sensors_with_distances =
      parse(content)
      |> get_sensors_with_distances()

    possible_locations =
      find_possible_locations_of_distress_beacon(sensors_with_distances, max_coord)

    location = find_location_of_distress_beacon(sensors_with_distances, possible_locations)

    result = get_tuning_frequency(location)

    {:ok, result}
  end

  defp parse(content) do
    AoC.Common.split_lines(content)
    |> Enum.map(fn line -> parse_line(line) end)
  end

  defp parse_line(line) do
    [_, sx, sy, bx, by] = Regex.run(@line_pattern, line)

    [sx, sy, bx, by] =
      [sx, sy, bx, by]
      |> Enum.map(fn str ->
        {int, _} = Integer.parse(str)
        int
      end)

    {{sx, sy}, {bx, by}}
  end

  defp count_non_beacons_positions_on_row(sensors_and_beacons, row) do
    sensors_with_distances = get_sensors_with_distances(sensors_and_beacons)
    beacon_positions = get_set_of_beacon_positions(sensors_and_beacons)
    {leftmost, rightmost} = find_leftmost_and_rightmost_points(sensors_with_distances)

    leftmost..rightmost
    |> Enum.filter(fn x ->
      coord = {x, row}

      Enum.any?(sensors_with_distances, fn {sensor, distance} ->
        AoC.Common.manhattan_distance(coord, sensor) <= distance and
          !MapSet.member?(beacon_positions, coord)
      end)
    end)
    |> Enum.count()
  end

  defp get_sensors_with_distances(sensors_and_beacons) do
    sensors_and_beacons
    |> Enum.map(fn {sensor, beacon} ->
      {sensor, AoC.Common.manhattan_distance(sensor, beacon)}
    end)
  end

  defp get_set_of_beacon_positions(sensors_and_beacons) do
    sensors_and_beacons |> Enum.map(fn {_, beacon} -> beacon end) |> MapSet.new()
  end

  defp find_leftmost_and_rightmost_points(sensors_with_distance) do
    sensors_with_distance
    |> Enum.reduce({nil, nil}, fn {{x, y}, dist}, {leftmost, rightmost} ->
      left_point = x - dist
      right_point = y + dist

      leftmost =
        cond do
          leftmost == nil -> left_point
          left_point < leftmost -> left_point
          true -> leftmost
        end

      rightmost =
        cond do
          rightmost == nil -> right_point
          right_point > rightmost -> right_point
          true -> rightmost
        end

      {leftmost, rightmost}
    end)
  end

  defp find_perimeter({x, y}, dist) do
    [
      {{x - dist, y}, {1, -1}},
      {{x, y - dist}, {1, 1}},
      {{x + dist, y}, {-1, 1}},
      {{x, y + dist}, {-1, -1}}
    ]
    |> Enum.reduce(MapSet.new(), fn {{x, y}, {dx, dy}}, set ->
      0..dist
      |> Enum.reduce(set, fn amt, set -> MapSet.put(set, {x + dx * amt, y + dy * amt}) end)
    end)
  end

  defp find_perimeter_within_bounds(point, distance, max_coord) do
    find_perimeter(point, distance)
    |> Enum.filter(fn {x, y} -> x >= 0 and x <= max_coord and y >= 0 and y <= max_coord end)
    |> MapSet.new()
  end

  defp get_tuning_frequency({x, y}) do
    x * 4_000_000 + y
  end

  defp find_possible_locations_of_distress_beacon(sensors_with_distances, max_coord) do
    sensors_with_distances
    |> Enum.reduce(MapSet.new(), fn {sensor, distance}, set ->
      MapSet.union(set, find_perimeter_within_bounds(sensor, distance + 1, max_coord))
    end)
  end

  defp find_location_of_distress_beacon(sensors_with_distances, possible_locations) do
    possible_locations
    |> Enum.find(fn point ->
      !(sensors_with_distances
        |> Enum.any?(fn {sensor, distance} ->
          AoC.Common.manhattan_distance(sensor, point) <= distance
        end))
    end)
  end
end
