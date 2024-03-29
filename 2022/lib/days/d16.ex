defmodule AoC.Days.D16 do
  @line_pattern ~r/Valve (.+) has flow rate=(\d+); tunnels? leads? to valves? (.+)/

  def part_one(content) do
    result = parse(content) |> maximum_pressure_release_alone()

    {:ok, result}
  end

  def part_two(content) do
    result = parse(content) |> maximum_pressure_release_in_seconds_with_elephant()

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
        {flow_rate, _} = Integer.parse(flow_rate)

        Map.put(
          valves,
          valve,
          {flow_rate, String.split(leads_to, ",") |> Enum.map(fn x -> String.trim(x) end)}
        )
    end
  end

  defp maximum_pressure_release_alone(valves) do
    seconds = 30
    start_valve = "AA"
    {_, costs} = get_positive_valves_and_costs(valves, start_valve)

    {max_pressure, _} =
      find_maximum_pressure(
        valves,
        costs,
        start_valve,
        seconds,
        seconds,
        [],
        0,
        %{}
      )

    max_pressure
  end

  defp maximum_pressure_release_in_seconds_with_elephant(valves) do
    seconds = 26
    start_valve = "AA"
    {_, costs} = get_positive_valves_and_costs(valves, start_valve)

    {_, possible_paths} =
      find_maximum_pressure(
        valves,
        costs,
        start_valve,
        seconds,
        seconds,
        [],
        0,
        %{}
      )

    result =
      possible_paths
      |> Enum.reduce(0, fn {a, av}, max ->
        possible_paths
        |> Enum.reduce(max, fn {b, bv}, max ->

          cond do
            MapSet.disjoint?(a, b) and av + bv > max -> av + bv
            true -> max
          end
        end)
      end)

    result
  end

  defp get_positive_valves_and_costs(valves, start_valve) do
    positive_valves = get_valves_with_positive_flow_rates(valves)

    dist_valves =
      if start_valve in positive_valves,
        do: positive_valves,
        else: [start_valve] ++ positive_valves

    costs =
      dist_valves
      |> Enum.reduce(%{}, fn a, dist_map ->
        distances_from_a =
          dist_valves
          |> Enum.filter(fn b -> b != a end)
          |> Enum.reduce(%{}, fn b, distances_from_a ->
            Map.put(distances_from_a, b, cost_to_open_valve(valves, a, b))
          end)

        Map.put(dist_map, a, distances_from_a)
      end)

    {positive_valves, costs}
  end

  defp get_valves_with_positive_flow_rates(valves) do
    Enum.filter(valves, fn {_, {flow_rate, _}} -> flow_rate > 0 end)
    |> Enum.map(fn {k, _} -> k end)
  end

  defp cost_to_open_valve(valves, a, b) do
    {:ok, path} =
      AoC.Common.Dijkstra.find_path(valves, a, b, fn valve, _ ->
        neighbours_of(valves, valve)
        |> Enum.reduce(%{}, fn n, map -> Map.put(map, n, 1) end)
      end)

    # distance to valve is length_of_path - 1 (starting point is included in
    # path), but cost to open that valve is distance + 1 (i.e. length of path)
    length(path)
  end

  defp neighbours_of(valves, valve) do
    {_, neighbours} = Map.get(valves, valve)
    neighbours
  end

  defp flow_rate_of(valves, valve) do
    {flow_rate, _} = Map.get(valves, valve)
    flow_rate
  end

  defp find_maximum_pressure(
         valves,
         costs,
         start_valve,
         maximum_seconds,
         seconds_remaining,
         current_path,
         current_max,
         possible_paths
       ) do
    current_valve = if length(current_path) == 0, do: start_valve, else: List.last(current_path)
    current_path_nodes = MapSet.new(current_path)

    released =
      calculate_pressure_released_from_path(
        valves,
        costs,
        current_path,
        maximum_seconds,
        start_valve
      )

    possible_paths =
      Map.put(
        possible_paths,
        current_path_nodes,
        max(Map.get(possible_paths, current_path_nodes, 0), released)
      )

    current_max = max(released, current_max)

    cond do
      seconds_remaining <= 0 ->
        {current_max, possible_paths}

      true ->
        current_valve_costs = Map.get(costs, current_valve)

        candidates =
          current_valve_costs
          |> Map.keys()
          |> Enum.filter(fn target_valve ->
            target_valve not in current_path
          end)

        case length(candidates) do
          0 ->
            {current_max, possible_paths}

          _ ->
            candidates
            |> Enum.reduce({current_max, possible_paths}, fn candidate_valve,
                                                             {current_max, possible_paths} ->
              {released, possible_paths} =
                find_maximum_pressure(
                  valves,
                  costs,
                  start_valve,
                  maximum_seconds,
                  seconds_remaining - Map.get(current_valve_costs, candidate_valve),
                  current_path ++ [candidate_valve],
                  current_max,
                  possible_paths
                )

              {max(released, current_max), possible_paths}
            end)
        end
    end
  end

  defp calculate_pressure_released_from_path(valves, costs, path, seconds, start_valve) do
    {_, _, total_released} =
      path
      |> Enum.reduce({start_valve, seconds, 0}, fn valve,
                                                   {current, current_seconds, total_released} ->
        cond do
          current == valve ->
            {valve, current_seconds, total_released}

          true ->
            current_costs = Map.get(costs, current)
            cost_to_open_valve = Map.get(current_costs, valve)
            flow_rate = flow_rate_of(valves, valve)
            released_overall = (current_seconds - cost_to_open_valve) * flow_rate
            {valve, current_seconds - cost_to_open_valve, total_released + released_overall}
        end
      end)

    total_released
  end
end
