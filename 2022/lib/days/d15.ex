defmodule AoC.Days.D15 do
  @line_pattern ~r/Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)/

  def part_one(content, target_row \\ 2_000_000) do
    result = parse(content) |> count_non_beacons_positions_on_row(target_row)

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

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
end
