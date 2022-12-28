defmodule AoC.Days.D16 do
  @line_pattern ~r/Valve (.+) has flow rate=(\d+); tunnels? leads? to valves? (.+)/

  def part_one(content) do
    result = parse(content) |> maximum_pressure_release_in_seconds(30)

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp parse(content) do
    String.trim(content)
    |> AoC.Common.split_lines()
    |> Enum.reduce(%{}, fn line, map -> parse_line(line, map) end)
  end

  defp parse_line(line, valves) do
    case Regex.run(@line_pattern, String.trim(line)) do
      nil ->
        {:error, :parse_error}

      [_, valve, flow_rate, leads_to] ->
        Map.put(
          valves,
          valve,
          {flow_rate, String.split(leads_to, ",") |> Enum.map(fn x -> String.trim(x) end)}
        )
    end
  end

  defp maximum_pressure_release_in_seconds(valves, seconds) do
    123
  end
end
