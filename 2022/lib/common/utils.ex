defmodule AoC.Common do
  def split_lines(content) do
    String.split(content, ~r/\r\n|\n|\r/)
  end

  def split_double_lines(content) do
    String.split(content, ~r/((\r\n){2}|(\n){2}|(\r){2})/)
  end

  def normalize_lines(content) do
    String.replace(content, ~r/\r\n|\r|\n/, "\n")
  end

  def split_by_space(content) do
    String.split(content, ~r/ +/)
  end

  def combine(first, second) do
    first
    |> Enum.reduce([], fn row, entries ->
      entries ++
        (second
         |> Enum.reduce([], fn col, entries ->
           entries ++ [{row, col}]
         end))
    end)
  end

  def characters(str) do
    String.to_charlist(str)
  end

  def dijkstra(graph, source, target, neighbour_fn) do
    {dist, prev} =
      graph
      |> Enum.reduce({%{}, %{}}, fn v, {dist, prev} ->
        {Map.put(dist, v, nil), Map.put(prev, v, nil)}
      end)

    dist = Map.put(dist, source, 0)

    dijkstra_internal(MapSet.new(graph), dist, prev, source, target, neighbour_fn)
  end

  defp dijkstra_internal(q, dist, prev, source, target, neighbour_fn) do
    cond do
      MapSet.size(q) == 0 ->
        {:error, :not_found}

      true ->
        u = dijkstra_next_in_queue(q, dist)

        cond do
          u == nil ->
            {:error, :no_path}

          u == target ->
            {:ok, dijkstra_calculate_path(source, target, prev)}

          true ->
            q = MapSet.difference(q, MapSet.new([u]))

            {dist, prev} =
              neighbour_fn.(u)
              |> Enum.filter(fn {v, _} -> MapSet.member?(q, v) end)
              |> Enum.reduce({dist, prev}, fn {v, cost}, {dist, prev} ->
                alt = Map.get(dist, u) + cost

                cond do
                  alt < Map.get(dist, v) ->
                    {Map.put(dist, v, alt), Map.put(prev, v, u)}

                  true ->
                    {dist, prev}
                end
              end)

            dijkstra_internal(q, dist, prev, source, target, neighbour_fn)
        end
    end
  end

  defp dijkstra_next_in_queue(q, dist) do
    Enum.reduce(q, nil, fn u, curr ->
      u_dist = Map.get(dist, u)
      curr_dist = Map.get(dist, curr)

      cond do
        curr_dist == nil and u_dist == nil -> nil
        curr_dist == nil and u_dist != nil -> u
        curr_dist != nil and u_dist == nil -> curr
        u_dist < curr_dist -> u
        u_dist >= curr_dist -> curr
      end
    end)
  end

  defp dijkstra_calculate_path(source, target, prev, path \\ []) do
    path =
      case length(path) do
        0 -> [target]
        _ -> path
      end

    cond do
      source == target or !Map.has_key?(prev, target) ->
        path

      true ->
        node = Map.get(prev, target)
        dijkstra_calculate_path(source, node, prev, [node | path])
    end
  end
end
