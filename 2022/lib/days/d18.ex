defmodule AoC.Days.D18 do
  def part_one(content) do
    result = parse(content) |> calculate_outer_surface_area(1)

    {:ok, result}
  end

  def part_two(_content) do
    result = -1

    {:ok, result}
  end

  defp parse(content) do
    content
    |> String.trim()
    |> AoC.Common.split_lines()
    |> Enum.map(fn line ->
      [x, y, z] =
        String.trim(line)
        |> String.split(",")
        |> Enum.map(fn x ->
          {i, _} = Integer.parse(x)

          i
        end)

      {x, y, z}
    end)
  end

  defp build_face_map(coords, cube_size) do
    coords
    |> Enum.reduce(%{}, fn coord, face_map ->
      Map.put(face_map, coord, MapSet.new(cube_faces(coord, cube_size)))
    end)
  end

  defp calculate_outer_surface_area(coords, cube_size) do
    coords = build_face_map(coords, cube_size)

    coords
    |> Enum.reduce(0, fn {coord, faces}, total_faces ->
      total_faces +
        (coords
         |> Enum.reduce(faces, fn {other, other_faces}, faces ->
           case other == coord do
             true ->
               faces

             false ->
               MapSet.difference(faces, other_faces)
           end
         end)
         |> MapSet.size())
    end)
  end

  defp cube_faces({x, y, z}, s) do
    [
      # front face
      MapSet.new([{x, y, z}, {x, y + s, z}, {x + s, y + s, z}, {x + s, y, z}]),
      # left face
      MapSet.new([{x, y, z + s}, {x, y + s, z + s}, {x, y + s, z}, {x, y, z}]),
      # back face
      MapSet.new([{x + s, y, z + s}, {x + s, y + s, z + s}, {x, y + s, z + s}, {x, y, z + s}]),
      # right face
      MapSet.new([{x + s, y, z}, {x + s, y + s, z}, {x + s, y + s, z + s}, {x + s, y, z + s}]),
      # top face
      MapSet.new([{x, y + s, z}, {x, y + s, z + s}, {x + s, y + s, z + s}, {x + s, y + s, z}]),
      # bottom face
      MapSet.new([{x + s, y, z}, {x + s, y, z + s}, {x, y, z + s}, {x, y, z}])
    ]
  end
end
