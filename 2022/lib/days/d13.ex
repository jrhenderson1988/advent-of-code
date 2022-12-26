defmodule AoC.Days.D13 do
  def part_one(content) do
    result =
      parse_packet_pairs(content)
      |> Enum.with_index()
      |> Enum.filter(fn {{left, right}, _} -> compare_packets(left, right) == :in_order end)
      |> Enum.map(fn {_, index} -> index + 1 end)
      |> Enum.sum()

    {:ok, result}
  end

  def part_two(content) do
    result =
      parse_all_packets(content)
      |> add_divider_packets()
      |> Enum.sort(fn a, b ->
        case compare_packets(a, b) do
          :in_order -> true
          :out_of_order -> false
          :equal -> true
        end
      end)
      |> calculate_decoder_key()

    {:ok, result}
  end

  defp parse_all_packets(content) do
    AoC.Common.split_lines(content)
    |> Enum.filter(fn line -> String.trim(line) != "" end)
    |> Enum.map(fn line -> parse_packet(line) end)
  end

  defp add_divider_packets(packets) do
    packets ++ [[[2]], [[6]]]
  end

  defp calculate_decoder_key(ordered_packets) do
    index_of_packet(ordered_packets, [[2]]) * index_of_packet(ordered_packets, [[6]])
  end

  defp index_of_packet(packets, search) do
    case Enum.with_index(packets) |> Enum.find(fn {packet, _} -> packet == search end) do
      nil -> :not_found
      {_, idx} -> idx + 1
    end
  end

  defp parse_packet_pairs(content) do
    content
    |> String.trim()
    |> AoC.Common.split_double_lines()
    |> Enum.map(fn chunk ->
      [left, right] =
        chunk
        |> String.trim()
        |> AoC.Common.split_lines()
        |> Enum.map(fn line -> parse_packet(line) end)

      {left, right}
    end)
  end

  defp parse_packet(line) do
    {:ok, packet} = Jason.decode(line)
    packet
  end

  defp compare_packets(left, right) when is_integer(left) and is_integer(right) do
    cond do
      left == right -> :equal
      left < right -> :in_order
      left > right -> :out_of_order
    end
  end

  defp compare_packets(left, right) when is_list(left) and is_integer(right),
    do: compare_packets(left, [right])

  defp compare_packets(left, right) when is_integer(left) and is_list(right),
    do: compare_packets([left], right)

  defp compare_packets(left, right) when is_list(left) and is_list(right) do
    left_len = length(left)
    right_len = length(right)
    limit = min(left_len, right_len) - 1

    in_order =
      if limit < 0 do
        :equal
      else
        0..limit
        |> Enum.reduce(:equal, fn idx, curr ->
          case curr do
            :equal ->
              compare_packets(Enum.at(left, idx), Enum.at(right, idx))

            other ->
              other
          end
        end)
      end

    case in_order do
      :equal ->
        cond do
          left_len == right_len -> :equal
          left_len < right_len -> :in_order
          left_len > right_len -> :out_of_order
        end

      other ->
        other
    end
  end
end
