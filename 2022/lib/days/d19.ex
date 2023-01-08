defmodule AoC.Days.D19 do
  @max_mins 24

  def part_one(content) do
    result = parse(content) |> determine_quality_level_sum()

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp parse(content) do
    content
    |> AoC.Common.split_lines()
    |> Enum.reduce(%{}, fn line, blueprints ->
      {id, blueprint} = parse_blueprint(line)
      Map.put(blueprints, id, blueprint)
    end)
  end

  defp parse_blueprint(line) do
    [blueprint_chunk, rest] = String.split(line, ":")
    {id, _} = Integer.parse(String.replace(blueprint_chunk, "Blueprint ", ""))

    robots =
      String.trim_trailing(rest, ".")
      |> String.split(".")
      |> Enum.reduce(%{}, fn robot_chunk, robots ->
        [type_chunk, cost_chunk] = robot_chunk |> String.split("costs")

        type =
          String.replace(type_chunk, "Each", "")
          |> String.replace("robot", "")
          |> String.trim()
          |> parse_material()

        costs =
          cost_chunk
          |> String.split("and")
          |> Enum.reduce(%{}, fn cost_chunk, costs ->
            [qty_chunk, mat_chunk] = String.trim(cost_chunk) |> String.split(" ")
            {qty, _} = Integer.parse(qty_chunk)
            material = parse_material(mat_chunk)
            Map.put(costs, material, Map.get(costs, material, 0) + qty)
          end)

        Map.put(robots, type, costs)
      end)

    {id, robots}
  end

  defp parse_material(str) do
    case str do
      "ore" -> :ore
      "clay" -> :clay
      "obsidian" -> :obsidian
      "geode" -> :geode
    end
  end

  defp determine_quality_level_sum(blueprints) do
    blueprints
    |> Enum.map(fn {id, blueprint} ->
      determine_quality_level(id, blueprint)
    end)
    |> Enum.sum()
  end

  defp determine_quality_level(id, blueprint) do
    max_spend_per_round = get_maximum_spend_per_round(blueprint)

    current_state = {{1, 0, 0, 0}, {0, 0, 0, 0}}

    {total, _} =
      total_geodes_possible(blueprint, max_spend_per_round, current_state, @max_mins, 0, %{})

    total * id
  end

  defp total_geodes_possible(
         blueprint,
         max_spend,
         state,
         minutes_remaining,
         max_geodes,
         cache
       ) do
    cond do
      minutes_remaining < 1 ->
        :error

      minutes_remaining == 1 ->
        new_state = gather_materials(state)
        total_geodes = get_material_quantity(new_state, :geode)
        result = max(total_geodes, max_geodes)

        key = {new_state, minutes_remaining}
        cache = Map.put(cache, key, result)
        {result, cache}

      minutes_remaining > 1 ->
        new_state = gather_materials(state)

        neighbours =
          get_neighbouring_states(blueprint, state, new_state)
          |> Enum.reject(fn neighbour -> too_many_robots(neighbour, max_spend) end)
          |> Enum.reject(fn neighbour ->
            cannot_beat_current_best(neighbour, minutes_remaining, max_geodes)
          end)
          |> Enum.map(fn neighbour ->
            discard_excess_materials(neighbour, max_spend, minutes_remaining)
          end)

        neighbours
        |> Enum.reduce({max_geodes, cache}, fn neighbour, {current_max, cache} ->
          new_minutes_remaining = minutes_remaining - 1

          key = {neighbour, new_minutes_remaining}

          cond do
            Map.has_key?(cache, key) ->
              {Map.get(cache, key), cache}

            true ->
              {result, cache} =
                total_geodes_possible(
                  blueprint,
                  max_spend,
                  neighbour,
                  new_minutes_remaining,
                  current_max,
                  cache
                )

              new_max = max(current_max, result)
              cache = Map.put(cache, key, new_max)
              {new_max, cache}
          end
        end)
    end
  end

  defp get_robot_total(state, robot) do
    {{ore, clay, obsidian, geode}, _} = state

    case robot do
      :ore -> ore
      :clay -> clay
      :obsidian -> obsidian
      :geode -> geode
    end
  end

  defp get_material_quantity(state, material) do
    {_, {ore, clay, obsidian, geode}} = state

    case material do
      :ore -> ore
      :clay -> clay
      :obsidian -> obsidian
      :geode -> geode
    end
  end

  defp get_neighbouring_states(blueprint, current_state, next_state) do
    [:geode, :obsidian, :clay, :ore, :none]
    |> Enum.filter(fn robot -> can_afford_robot(blueprint, current_state, robot) end)
    |> Enum.map(fn robot ->
      cost = get_robot_cost(blueprint, robot)

      next_state |> charge_cost(cost) |> add_robot(robot)
    end)
  end

  defp can_afford_robot(blueprint, state, robot) do
    costs = get_robot_cost(blueprint, robot)

    Enum.all?(costs, fn {material, required_quantity} ->
      get_material_quantity(state, material) >= required_quantity
    end)
  end

  defp get_robot_cost(blueprint, robot) do
    case robot do
      :none -> %{}
      robot -> Map.get(blueprint, robot)
    end
  end

  defp charge_cost(state, cost) do
    {robots, materials} = state

    materials =
      cost
      |> Enum.reduce(materials, fn {material, quantity}, {ore, clay, obsidian, geode} ->
        case material do
          :ore -> {ore - quantity, clay, obsidian, geode}
          :clay -> {ore, clay - quantity, obsidian, geode}
          :obsidian -> {ore, clay, obsidian - quantity, geode}
          :geode -> {ore, clay, obsidian, geode - quantity}
        end
      end)

    {robots, materials}
  end

  defp add_robot(state, robot) do
    case robot do
      :none ->
        state

      robot ->
        {{ore, clay, obsidian, geode}, materials} = state

        robots =
          case robot do
            :ore -> {ore + 1, clay, obsidian, geode}
            :clay -> {ore, clay + 1, obsidian, geode}
            :obsidian -> {ore, clay, obsidian + 1, geode}
            :geode -> {ore, clay, obsidian, geode + 1}
          end

        {robots, materials}
    end
  end

  defp gather_materials(state) do
    {robots, materials} = state
    {r_ore, r_clay, r_obsidian, r_geode} = robots
    {m_ore, m_clay, m_obsidian, m_geode} = materials

    {robots, {m_ore + r_ore, m_clay + r_clay, m_obsidian + r_obsidian, m_geode + r_geode}}
  end

  defp get_maximum_spend_per_round(blueprint) do
    [:ore, :clay, :obsidian]
    |> Enum.reduce(%{}, fn material, max_materials ->
      Map.put(
        max_materials,
        material,
        Enum.map(blueprint, fn {_, cost} -> Map.get(cost, material, 0) end) |> Enum.max()
      )
    end)
  end

  def too_many_robots(state, max_spend) do
    max_spend
    |> Enum.any?(fn {material, max_required} ->
      get_robot_total(state, material) > max_required
    end)
  end

  def too_many_materials(state, max_spend) do
    max_spend
    |> Enum.any?(fn {material, max_required} ->
      get_material_quantity(state, material) > max_required
    end)
  end

  def discard_excess_materials(state, max_spend, minutes_remaining) do
    [new_ore, new_clay, new_obsidian] =
      [:ore, :clay, :obsidian]
      |> Enum.map(fn material ->
        current = get_material_quantity(state, material)
        max_spend_per_round = Map.get(max_spend, material, current)
        max_spend_for_rest_of_simulation = max_spend_per_round * minutes_remaining

        min(current, max_spend_for_rest_of_simulation)
      end)

    {robots, _} = state

    {robots, {new_ore, new_clay, new_obsidian, get_material_quantity(state, :geode)}}
  end

  defp cannot_beat_current_best(state, minutes_remaining, current_best) do
    optimistic_best_score(state, minutes_remaining) < current_best
  end

  defp optimistic_best_score(state, minutes_remaining) do
    current_geode_robots = get_robot_total(state, :geode)
    current_geodes = get_material_quantity(state, :geode)

    current_geodes +
      (0..(minutes_remaining - 1)
       |> Enum.reduce(current_geode_robots, fn n, geode_robots ->
         geode_robots + n
       end))
  end
end
