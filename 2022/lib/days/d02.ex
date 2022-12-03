defmodule AoC.Days.D02 do
  def part_one(content) do
    result =
      content
      |> AoC.Common.split_lines()
      |> Enum.map(fn line ->
        [opponent_input, player_input] = AoC.Common.split_by_space(line)

        with {:ok, opponent} <- get_opponent_move(opponent_input),
             {:ok, player} <- get_player_move(player_input),
             {:ok, round_outcome} <- get_round_outcome(opponent, player) do
          get_move_value(player) + get_round_outcome_value(round_outcome)
        end
      end)
      |> Enum.sum()

    {:ok, result}
  end

  def part_two(content) do
    result =
      content
      |> AoC.Common.split_lines()
      |> Enum.map(fn line ->
        [opponent_input, desired_outcome] = AoC.Common.split_by_space(line)

        with {:ok, opponent_move} <- get_opponent_move(opponent_input),
             {:ok, desired_outcome} <- get_desired_outcome(desired_outcome),
             {:ok, player_move} <- get_move_for_desired_outcome(opponent_move, desired_outcome) do
          get_move_value(player_move) + get_round_outcome_value(desired_outcome)
        end
      end)
      |> Enum.sum()

    {:ok, result}
  end

  defp get_opponent_move(input) do
    case input do
      "A" -> {:ok, :rock}
      "B" -> {:ok, :paper}
      "C" -> {:ok, :scissors}
      _ -> {:error, :invalid_opponent_move}
    end
  end

  defp get_player_move(input) do
    case input do
      "X" -> {:ok, :rock}
      "Y" -> {:ok, :paper}
      "Z" -> {:ok, :scissors}
    end
  end

  defp get_desired_outcome(input) do
    case input do
      "X" -> {:ok, :loss}
      "Y" -> {:ok, :draw}
      "Z" -> {:ok, :win}
    end
  end

  defp get_round_outcome(opponent_move, player_move) do
    case opponent_move do
      :rock ->
        case player_move do
          :rock -> {:ok, :draw}
          :paper -> {:ok, :win}
          :scissors -> {:ok, :loss}
        end

      :paper ->
        case player_move do
          :rock -> {:ok, :loss}
          :paper -> {:ok, :draw}
          :scissors -> {:ok, :win}
        end

      :scissors ->
        case player_move do
          :rock -> {:ok, :win}
          :paper -> {:ok, :loss}
          :scissors -> {:ok, :draw}
        end
    end
  end

  defp get_move_for_desired_outcome(opponent_move, desired_outcome) do
    case desired_outcome do
      :draw ->
        {:ok, opponent_move}

      :win ->
        case opponent_move do
          :rock -> {:ok, :paper}
          :paper -> {:ok, :scissors}
          :scissors -> {:ok, :rock}
        end

      :loss ->
        case opponent_move do
          :paper -> {:ok, :rock}
          :scissors -> {:ok, :paper}
          :rock -> {:ok, :scissors}
        end
    end
  end

  defp get_move_value(player_move) do
    case player_move do
      :rock -> 1
      :paper -> 2
      :scissors -> 3
    end
  end

  defp get_round_outcome_value(round_outcome) do
    case round_outcome do
      :win -> 6
      :draw -> 3
      :loss -> 0
    end
  end
end
