defmodule AoC.Days.D19 do
  @max_mins 22

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
    # TODO - remove take to try all blueprints
    |> Enum.take(1)
    |> Enum.map(fn {id, blueprint} ->
      IO.puts("==== QUALITY FOR BLUEPRINT #{id} ====")
      determine_quality_level(id, blueprint)
    end)
    |> Enum.sum()
  end

  defp determine_quality_level(id, blueprint) do
    current_state = {{1, 0, 0, 0}, {0, 0, 0, 0}}
    total = total_geodes_possible(blueprint, current_state, @max_mins, 0)
    total * id
  end

  defp total_geodes_possible(blueprint, state, minutes_remaining, max_geodes) do
    # indent = 0..(@max_mins - minutes_remaining) |> Enum.map(fn _ -> "  " end) |> Enum.join()
    # IO.puts("#{indent}Minutes remaining: #{minutes_remaining}, state: #{inspect(state)}")

    cond do
      minutes_remaining < 0 ->
        :error

      minutes_remaining == 0 ->
        total_geodes = get_material_quantity(state, :geode)
        max(total_geodes, max_geodes)

      minutes_remaining > 0 ->
        neighbours = get_neighbouring_states(blueprint, state)

        neighbours
        |> Enum.reduce(max_geodes, fn neighbour, current_max ->
          max(
            current_max,
            total_geodes_possible(blueprint, neighbour, minutes_remaining - 1, current_max)
          )
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

  defp get_neighbouring_states(blueprint, state) do
    # all possible combinations of possible robot purchases
    # no robots purchased
    [:geode, :obsidian, :clay, :ore, :none]
    |> Enum.filter(fn robot -> can_afford_robot(blueprint, state, robot) end)
    |> Enum.map(fn robot ->
      cost = get_robot_cost(blueprint, robot)
      state = charge_cost(state, cost)
      state = gather_materials(state)
      state = add_robot(state, robot)

      state
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

  # defp buy_robot(blueprint, state, robot) do
  #   case robot do
  #     :none ->
  #       state

  #     robot ->
  #       state
  #       |> add_robot(robot)
  #       |> charge_cost(get_robot_cost(blueprint, robot))
  #   end
  # end

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
end
