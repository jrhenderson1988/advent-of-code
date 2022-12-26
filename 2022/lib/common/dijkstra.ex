defmodule AoC.Common.Dijkstra do
  alias AoC.Common.PriorityQueue, as: PQ

  def find_path(graph, source, target, neighbour_fn) do
    dist = %{source => 0}
    prev = %{}
    q = PQ.add_with_priority(PQ.new(), source, 0)

    loop(
      q,
      graph,
      dist,
      prev,
      source,
      target,
      neighbour_fn
    )
  end

  defp calculate_path(source, target, prev, path \\ []) do
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
        calculate_path(source, node, prev, [node | path])
    end
  end

  defp loop(q, graph, dist, prev, source, target, neighbour_fn) do
    cond do
      PQ.size(q) == 0 ->
        {:error, :not_found}

      true ->
        {{p, u}, q} = PQ.extract_min(q)

        cond do
          u == nil ->
            {:error, :no_path}

          u == target ->
            {:ok, calculate_path(source, target, prev)}

          p == Map.get(dist, u) ->
            {dist, prev, q} =
              neighbour_fn.(u, graph)
              |> Enum.reduce({dist, prev, q}, fn {v, cost}, {dist, prev, q} ->
                alt = Map.get(dist, u) + cost

                cond do
                  # if value not in dist, returning alt + 1 will make this always true
                  alt < Map.get(dist, v, alt + 1) ->
                    {Map.put(dist, v, alt), Map.put(prev, v, u), PQ.add_with_priority(q, v, alt)}

                  true ->
                    {dist, prev, q}
                end
              end)

            loop(q, graph, dist, prev, source, target, neighbour_fn)

          true ->
            loop(q, graph, dist, prev, source, target, neighbour_fn)
        end
    end
  end
end
