defmodule AoC.Days.D18 do
  def part_one(content) do
    result = parse(content) |> calculate_surface_area()

    {:ok, result}
  end

  def part_two(content) do
    result = parse(content) |> calculate_outer_surface_area()

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

  defp build_face_map(coords) do
    coords
    |> Enum.reduce(%{}, fn coord, face_map ->
      Map.put(face_map, coord, MapSet.new(cube_faces(coord)))
    end)
  end

  defp calculate_surface_area(coords) do
    cubes = build_face_map(coords)

    cubes
    |> Enum.reduce(0, fn {coord, faces}, total_faces ->
      total_faces +
        (cubes
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

  defp calculate_outer_surface_area(coords) do
    cubes = MapSet.new(coords)
    bounding_box = find_bounding_box(cubes, 1)

    flooded = flood_bounding_box(bounding_box, cubes)
    diff = extract_contained_shape(bounding_box, flooded)

    calculate_surface_area(diff |> MapSet.to_list())
  end

  defp flood_bounding_box(bounding_box, coords) do
    {start, _} = bounding_box
    flood(bounding_box, coords, start, MapSet.new())
  end

  defp flood(bounding_box, cubes, current, flooded) do
    flooded =
      cond do
        in_bounds?(bounding_box, current) and !MapSet.member?(cubes, current) ->
          MapSet.put(flooded, current)

        true ->
          flooded
      end

    neighbours_of(current)
    |> Enum.reject(fn neighbour -> MapSet.member?(flooded, neighbour) end)
    |> Enum.reject(fn neighbour -> MapSet.member?(cubes, neighbour) end)
    |> Enum.filter(fn neighbour -> in_bounds?(bounding_box, neighbour) end)
    |> Enum.reduce(flooded, fn neighbour, flooded ->
      MapSet.union(flooded, flood(bounding_box, cubes, neighbour, flooded))
    end)
  end

  defp find_bounding_box(cubes, padding) do
    {{sx, sy, sz}, {lx, ly, lz}} =
      MapSet.to_list(cubes)
      |> Enum.reduce({nil, nil}, fn {x, y, z}, {smallest, largest} ->
        smallest =
          case smallest do
            nil -> {x, y, z}
            {sx, sy, sz} -> {min(x, sx), min(y, sy), min(z, sz)}
          end

        largest =
          case largest do
            nil -> {x, y, z}
            {lx, ly, lz} -> {max(x, lx), max(y, ly), max(z, lz)}
          end

        {smallest, largest}
      end)

    {{sx - padding, sy - padding, sz - padding}, {lx + padding, ly + padding, lz + padding}}
  end

  defp neighbours_of({x, y, z}) do
    [{x + 1, y, z}, {x - 1, y, z}, {x, y + 1, z}, {x, y - 1, z}, {x, y, z + 1}, {x, y, z - 1}]
  end

  defp in_bounds?({{min_x, min_y, min_z}, {max_x, max_y, max_z}}, {x, y, z}) do
    x >= min_x and x <= max_x and y >= min_y and y <= max_y and z >= min_z and z <= max_z
  end

  defp cube_faces({x, y, z}) do
    [
      # front face
      MapSet.new([{x, y, z}, {x, y + 1, z}, {x + 1, y + 1, z}, {x + 1, y, z}]),
      # left face
      MapSet.new([{x, y, z + 1}, {x, y + 1, z + 1}, {x, y + 1, z}, {x, y, z}]),
      # back face
      MapSet.new([{x + 1, y, z + 1}, {x + 1, y + 1, z + 1}, {x, y + 1, z + 1}, {x, y, z + 1}]),
      # right face
      MapSet.new([{x + 1, y, z}, {x + 1, y + 1, z}, {x + 1, y + 1, z + 1}, {x + 1, y, z + 1}]),
      # top face
      MapSet.new([{x, y + 1, z}, {x, y + 1, z + 1}, {x + 1, y + 1, z + 1}, {x + 1, y + 1, z}]),
      # bottom face
      MapSet.new([{x + 1, y, z}, {x + 1, y, z + 1}, {x, y, z + 1}, {x, y, z}])
    ]
  end

  defp extract_contained_shape({{sx, sy, sz}, {lx, ly, lz}}, flooded_box) do
    filled =
      sx..lx
      |> Enum.reduce(MapSet.new(), fn x, filled ->
        sy..ly
        |> Enum.reduce(filled, fn y, filled ->
          sz..lz
          |> Enum.reduce(filled, fn z, filled ->
            MapSet.put(filled, {x, y, z})
          end)
        end)
      end)

    MapSet.difference(filled, flooded_box)
  end
end
