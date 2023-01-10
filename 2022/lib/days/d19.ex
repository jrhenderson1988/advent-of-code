defmodule AoC.Days.D19 do
  def part_one(content) do
    result = parse(content) |> determine_quality_level_sum()

    {:ok, result}
  end

  def part_two(content) do
    result = parse(content) |> product_of_maximum_opened_geodes()

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

  defp product_of_maximum_opened_geodes(blueprints) do
    blueprints
    |> Enum.take(3)
    |> Enum.map(fn {_, bp} -> adapt_blueprint(bp) end)
    |> Enum.map(fn bp ->
      r = search(bp, {{1, 0, 0, 0}, {0, 0, 0, 0}, 32}, 0)
      IO.puts("Done: #{r}")
      r
    end)
    |> Enum.product()
  end

  defp determine_quality_level_sum(blueprints) do
    blueprints
    |> Enum.map(fn {id, bp} -> {id, adapt_blueprint(bp)} end)
    |> Enum.map(fn {id, bp} -> determine_quality_level(id, bp) end)
    |> Enum.sum()
  end

  defp determine_quality_level(id, blueprint) do
    total = search(blueprint, {{1, 0, 0, 0}, {0, 0, 0, 0}, 24}, 0)
    total * id
  end

  defp adapt_blueprint(blueprint) do
    ore_cost = Map.get(blueprint, :ore)
    clay_cost = Map.get(blueprint, :clay)
    obsidian_cost = Map.get(blueprint, :obsidian)
    geode_cost = Map.get(blueprint, :geode)

    {
      Map.get(ore_cost, :ore),
      Map.get(clay_cost, :ore),
      Map.get(obsidian_cost, :ore),
      Map.get(obsidian_cost, :clay),
      Map.get(geode_cost, :ore),
      Map.get(geode_cost, :obsidian)
    }
  end

  defp search(bp, state, best_overall) do
    {robots, materials, time_remaining} = state
    {r_ore, r_clay, r_obsidian, r_geode} = robots
    {m_ore, m_clay, m_obsidian, m_geode} = materials

    cond do
      time_remaining == 0 ->
        0

      best_overall >= m_geode + optimistic_best(r_geode, time_remaining) ->
        0

      r_ore >= cost_of(bp, :geode, :ore) and r_obsidian >= cost_of(bp, :geode, :obsidian) ->
        optimistic_best(r_geode, time_remaining)

      true ->
        ore_limit_hit = r_ore >= max_spend(bp, :ore)
        clay_limit_hit = r_clay >= max_spend(bp, :clay)
        obsidian_limit_hit = r_obsidian >= max_spend(bp, :obsidian)

        %{
          :geode =>
            m_ore >= cost_of(bp, :geode, :ore) and m_obsidian >= cost_of(bp, :geode, :obsidian),
          :obsidian =>
            m_ore >= cost_of(bp, :obsidian, :ore) and m_clay >= cost_of(bp, :obsidian, :clay) and
              !obsidian_limit_hit,
          :clay => m_ore >= cost_of(bp, :clay, :ore) and !clay_limit_hit,
          :ore => m_ore >= cost_of(bp, :ore, :ore) and !ore_limit_hit,
          :none => !ore_limit_hit
        }
        |> Enum.reduce(0, fn {choice, condition}, best ->
          cond do
            condition ->
              new_state = choose_robot(bp, state, choice)
              max(best, r_geode + search(bp, new_state, max(best, best_overall)))

            true ->
              best
          end
        end)
    end
  end

  defp max_spend(bp, type) do
    case type do
      :ore ->
        max(
          cost_of(bp, :geode, :ore),
          max(cost_of(bp, :clay, :ore), cost_of(bp, :obsidian, :ore))
        )

      :clay ->
        cost_of(bp, :obsidian, :clay)

      :obsidian ->
        cost_of(bp, :geode, :obsidian)
    end
  end

  defp choose_robot(bp, state, robot) do
    {robots, materials, time_remaining} = state

    new_materials = gather_materials(bp, materials, robots, robot)
    new_robots = add_robot(robots, robot)
    {new_robots, new_materials, time_remaining - 1}
  end

  defp add_robot(robots, robot) do
    {r_ore, r_clay, r_obs, r_geo} = robots

    case robot do
      :none -> {r_ore, r_clay, r_obs, r_geo}
      :ore -> {r_ore + 1, r_clay, r_obs, r_geo}
      :clay -> {r_ore, r_clay + 1, r_obs, r_geo}
      :obsidian -> {r_ore, r_clay, r_obs + 1, r_geo}
      :geode -> {r_ore, r_clay, r_obs, r_geo + 1}
    end
  end

  defp gather_materials(bp, materials, robots, chosen_robot) do
    {r_ore, r_clay, r_obs, r_geo} = robots
    {m_ore, m_clay, m_obs, m_geo} = materials

    case chosen_robot do
      :none ->
        {r_ore + m_ore, m_clay + r_clay, m_obs + r_obs, m_geo + r_geo}

      :ore ->
        {r_ore + m_ore - cost_of(bp, :ore, :ore), m_clay + r_clay, m_obs + r_obs, m_geo + r_geo}

      :clay ->
        {r_ore + m_ore - cost_of(bp, :clay, :ore), m_clay + r_clay, m_obs + r_obs, m_geo + r_geo}

      :obsidian ->
        {r_ore + m_ore - cost_of(bp, :obsidian, :ore),
         m_clay + r_clay - cost_of(bp, :obsidian, :clay), m_obs + r_obs, m_geo + r_geo}

      :geode ->
        {r_ore + m_ore - cost_of(bp, :geode, :ore), m_clay + r_clay,
         m_obs + r_obs - cost_of(bp, :geode, :obsidian), m_geo + r_geo}
    end
  end

  defp optimistic_best(geode_robots, time_remaining) do
    sum_range(geode_robots, geode_robots + time_remaining - 1)
  end

  def sum_range(first, last) do
    sum_natural(last) - sum_natural(first - 1)
  end

  defp sum_natural(n) do
    n * (n + 1) / 2
  end

  def rs(geode_robots, minutes_remaining) do
    0..minutes_remaining
    |> Enum.reduce(geode_robots, fn mr, total -> total + (mr + geode_robots) end)
  end

  defp cost_of(blueprint, robot, material) do
    {ore_ore, clay_ore, obsidian_ore, obsidian_clay, geode_ore, geode_obsidian} = blueprint

    case {robot, material} do
      {:ore, :ore} -> ore_ore
      {:clay, :ore} -> clay_ore
      {:obsidian, :ore} -> obsidian_ore
      {:obsidian, :clay} -> obsidian_clay
      {:geode, :ore} -> geode_ore
      {:geode, :obsidian} -> geode_obsidian
    end
  end
end
