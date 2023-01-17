defmodule AoC.Days.D23 do
  @rules [:n, :s, :w, :e]
  @proposal_n MapSet.new([:n, :ne, :nw])
  @proposal_s MapSet.new([:s, :se, :sw])
  @proposal_w MapSet.new([:w, :nw, :sw])
  @proposal_e MapSet.new([:e, :ne, :se])

  def part_one(content) do
    result = parse(content) |> simulate(10) |> total_tiles_in_smallest_rectangle()

    {:ok, result}
  end

  def part_two(content) do
    result = parse(content) |> round_where_no_moves_occurred()

    {:ok, result}
  end

  defp round_where_no_moves_occurred(elves, round \\ 0) do
    {elves, moves} = simulate_round(round, elves)

    case moves do
      0 -> round + 1
      _ -> round_where_no_moves_occurred(elves, round + 1)
    end
  end

  defp simulate(elves, rounds) do
    0..(rounds - 1)
    |> Enum.reduce(elves, fn round, elves ->
      {elves, _} = simulate_round(round, elves)
      elves
    end)
  end

  defp simulate_round(round, elves) do
    proposals = get_proposals(elves, round)
    move_elves(elves, proposals)
  end

  defp get_proposals(elves, round) do
    elves |> Enum.map(fn elf -> propose(elf, elves, round) end)
  end

  defp get_targets(elves, proposals) do
    0..(length(elves) - 1)
    |> Enum.map(fn i ->
      proposal = Enum.at(proposals, i)

      case proposal do
        :none -> nil
        proposal -> get_adjacent(Enum.at(elves, i), proposal)
      end
    end)
  end

  defp get_other_targets(targets, i) do
    pre = if(i == 0, do: [], else: Enum.slice(targets, 0..(i - 1)))
    post = Enum.slice(targets, (i + 1)..-1)

    (pre ++ post)
    |> Enum.reject(&is_nil/1)
    |> MapSet.new()
  end

  defp is_unique_target(targets, i) do
    target = Enum.at(targets, i)
    other_targets = get_other_targets(targets, i)
    !MapSet.member?(other_targets, target)
  end

  defp move_elves(elves, proposals) do
    targets = get_targets(elves, proposals)
    0..(length(elves) - 1)
    |> Enum.reduce({[], 0}, fn i, {updated, moves} ->
      elf = Enum.at(elves, i)
      target = Enum.at(targets, i)

      cond do
        target == nil -> {updated ++ [elf], moves}
        is_unique_target(targets, i) -> {updated ++ [target], moves + 1}
        true -> {updated ++ [elf], moves}
      end
    end)
  end

  defp propose(elf, elves, round) do
    neighbours = get_all_neighbours(elf, elves)

    cond do
      MapSet.size(neighbours) == 0 ->
        :none

      true ->
        0..(length(@rules) - 1)
        |> Enum.map(fn i -> select_rule(round, i) end)
        |> Enum.find(:none, fn proposal -> is_proposal_valid(neighbours, proposal) end)
    end
  end

  defp select_rule(round, i) do
    Enum.at(@rules, rem(round + i, length(@rules)))
  end

  defp is_proposal_valid(neighbours, proposal) do
    adjacents =
      case proposal do
        :n -> @proposal_n
        :s -> @proposal_s
        :w -> @proposal_w
        :e -> @proposal_e
      end

    adjacents |> MapSet.disjoint?(neighbours)
  end

  defp get_all_neighbours(elf, elves) do
    [:n, :ne, :e, :se, :s, :sw, :w, :nw]
    |> Enum.filter(fn dir -> Enum.member?(elves, get_adjacent(elf, dir)) end)
    |> MapSet.new()
  end

  defp dir_to_delta(dir) do
    case dir do
      :n -> {0, -1}
      :ne -> {1, -1}
      :e -> {1, 0}
      :se -> {1, 1}
      :s -> {0, 1}
      :sw -> {-1, 1}
      :w -> {-1, 0}
      :nw -> {-1, -1}
    end
  end

  defp get_adjacent({x, y}, dir) do
    {dx, dy} = dir_to_delta(dir)
    {x + dx, y + dy}
  end

  defp total_tiles_in_smallest_rectangle(elves) do
    {{min_x, min_y}, {max_x, max_y}} =
      elves
      |> Enum.reduce({nil, nil}, fn {x, y}, {min_pos, max_pos} ->
        min_pos =
          case min_pos do
            nil -> {x, y}
            {min_x, min_y} -> {min(min_x, x), min(min_y, y)}
          end

        max_pos =
          case max_pos do
            nil -> {x, y}
            {max_x, max_y} -> {max(max_x, x), max(max_y, y)}
          end

        {min_pos, max_pos}
      end)

    (max_x - min_x + 1) * (max_y - min_y + 1) - length(elves)
  end

  defp parse(content) do
    String.trim(content)
    |> AoC.Common.split_lines()
    |> Enum.with_index()
    |> Enum.reduce([], fn {line, y}, elves ->
      String.trim(line)
      |> AoC.Common.characters()
      |> Enum.with_index()
      |> Enum.reduce(elves, fn {ch, x}, elves ->
        case ch do
          ?# -> [{x, y} | elves]
          _ -> elves
        end
      end)
    end)
  end

  # defp draw_elves(elves) do
  #   {{min_x, min_y}, {max_x, max_y}} =
  #     elves
  #     |> Enum.reduce({nil, nil}, fn {x, y}, {smallest, largest} ->
  #       smallest =
  #         case smallest do
  #           nil -> {x, y}
  #           {sx, sy} -> {min(sx, x), min(sy, y)}
  #         end

  #       largest =
  #         case largest do
  #           nil -> {x, y}
  #           {sx, sy} -> {max(sx, x), max(sy, y)}
  #         end

  #       {smallest, largest}
  #     end)

  #   (min_y - 1)..(max_y + 1)
  #   |> Enum.reduce("", fn y, s ->
  #     row =
  #       (min_x - 1)..(max_x + 1)
  #       |> Enum.reduce(s, fn x, s ->
  #         s <>
  #           case Enum.member?(elves, {x, y}) do
  #             true -> "#"
  #             false -> "."
  #           end
  #       end)

  #     row <> "\n"
  #   end)
  # end
end
