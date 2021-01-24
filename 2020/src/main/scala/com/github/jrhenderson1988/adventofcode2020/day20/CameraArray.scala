package com.github.jrhenderson1988.adventofcode2020.day20

case class CameraArray(tiles: List[Tile]) {
  val edgeCounts: Map[Int, Int] = tiles
    .flatMap(tile => tile.allEdges)
    .groupBy(edge => edge)
    .map { case (edge, values) => (edge, values.size) }

  val edgesToTileIds: Map[Int, List[Long]] = tiles
    .flatMap(tile => tile.allEdges.map(edge => (edge -> tile.id)))
    .groupBy { case (edge, _) => edge }
    .map { case (edge, pairs) => edge -> pairs.map(_._2) }

  val tilesById: Map[Long, Tile] = tiles
    .map { tile => tile.id -> tile }
    .toMap

  val seaMonster: List[(Int, Int)] =
    """                  #
      |#    ##    ##    ###
      | #  #  #  #  #  #   """
      .stripMargin
      .split("\\n+")
      .zipWithIndex
      .map { case (line, row) =>
        line.toCharArray.zipWithIndex.filter { case (ch, _) => ch == '#' }.map { case (_, col) => (row, col) }
      }
      .foldLeft(List[(Int, Int)]()) { (acc, row) => acc ++ row }

  def productOfCornerIds(): Long = getCorners.map(_.id).product

  def waterRoughness(): Int = {
    val grid = buildGrid()
    val image = buildImageFromGrid(grid)
    val seaMonsterPointCount = orientAndCountSeaMonsterPoints(image)
    image.cells.map(row => row.count(cell => cell)).sum - seaMonsterPointCount
  }

  def getCorners: List[Tile] = {
    tiles.filter { tile =>
      val others = tiles.diff(List(tile))
      List(tile, tile.flip())
        .exists { tile =>
          tile
            .edges
            .count { edge => others.exists(other => other.hasMatchingEdgeFor(edge)) } == 2
        }
    }
  }

  private def buildImageFromGrid(grid: List[List[Tile]]): Tile = {
    val gridWithoutBorders = grid.map(row => row.map(tile => tile.withoutBorder()))
    val tileSize = gridWithoutBorders.head.head.cells.size

    Tile(
      0,
      gridWithoutBorders
        .map { gridRow =>
          (0 until tileSize)
            .map { tileRowIndex =>
              gridRow
                .map { tile => tile.cells(tileRowIndex) }
                .foldLeft(List[Boolean]()) { (acc, tileRow) => acc ++ tileRow }
            }
            .toList
        }
        .foldLeft(List[List[Boolean]]()) { (acc, row) => acc ++ row }
    )
  }

  private def buildGrid(): List[List[Tile]] = {
    val gridSize = Math.sqrt(tiles.size).toInt
    val grid = Array.ofDim[Tile](gridSize, gridSize)
    val corners = getCorners.map(tile => tile.id)
    var placed = List[Long]()

    (0 until gridSize).foreach { y =>
      (0 until gridSize).foreach { x =>
        grid(y)(x) = (x, y) match {
          // Top left corner
          case (x, y) if x == 0 && y == 0 =>
            orientTile(tilesById(corners.head), tile =>
              edgeCounts(tile.getEdge(Edge.LEFT)) == 1 &&
                edgeCounts(tile.getEdge(Edge.TOP)) == 1 &&
                edgeCounts(tile.getEdge(Edge.RIGHT)) > 1 &&
                edgeCounts(tile.getEdge(Edge.BOTTOM)) > 1
            )

          // Top right corner
          case (x, y) if x == gridSize - 1 && y == 0 =>
            val (targetEdge, tile) = findTarget(Edge.LEFT, x, y, grid, placed, id => corners.contains(id))
            orientTile(tile, t => t.getEdge(Edge.LEFT) == targetEdge && edgeCounts(tile.getEdge(Edge.BOTTOM)) > 1)

          // Bottom right corner
          case (x, y) if x == gridSize - 1 && y == gridSize - 1 =>
            val (targetEdges, tile) = findTargets(List(Edge.LEFT, Edge.TOP), x, y, grid, placed, id => corners.contains(id))
            orientTile(tile, t => t.getEdge(Edge.TOP) == targetEdges.last && t.getEdge(Edge.LEFT) == targetEdges.head)

          // Bottom left corner
          case (x, y) if x == 0 && y == gridSize - 1 =>
            val (targetEdge, tile) = findTarget(Edge.TOP, x, y, grid, placed, id => corners.contains(id))
            orientTile(
              tile,
              t => t.getEdge(Edge.TOP) == targetEdge && edgeCounts(t.getEdge(Edge.RIGHT)) > 1
            )

          // Top row, non-corners
          case (x, y) if y == 0 =>
            val (targetEdge, tile) = findTarget(Edge.LEFT, x, y, grid, placed, _ => true)
            orientTile(tile, t => t.getEdge(Edge.LEFT) == targetEdge)

          // Left edge
          case (x, y) if x == 0 =>
            val (targetEdge, tile) = findTarget(Edge.TOP, x, y, grid, placed, _ => true)
            orientTile(tile, t => t.getEdge(Edge.TOP) == targetEdge)

          // Others
          case (x, y) =>
            val (targetEdges, tile) = findTargets(List(Edge.LEFT, Edge.TOP), x, y, grid, placed, _ => true)
            orientTile(
              tile,
              t => t.getEdge(Edge.LEFT) == targetEdges.head && t.getEdge(Edge.TOP) == targetEdges.last
            )
        }

        placed = placed ++ List(grid(y)(x).id)
      }
    }

    grid.map(_.toList).toList
  }

  private def findTargets(edges: List[Edge], x: Int, y: Int, grid: Array[Array[Tile]], placed: List[Long], filter: Long => Boolean): (List[Int], Tile) = {
    val targetEdges = edges.map { edge =>
      val (dx, dy) = edge match {
        case Edge.TOP => (0, -1)
        case Edge.RIGHT => (1, 0)
        case Edge.BOTTOM => (0, 1)
        case Edge.LEFT => (-1, 0)
      }

      grid(y + dy)(x + dx).getOppositeEdge(
        edge match {
          case Edge.TOP => Edge.BOTTOM
          case Edge.RIGHT => Edge.LEFT
          case Edge.BOTTOM => Edge.TOP
          case Edge.LEFT => Edge.RIGHT
        }
      )
    }

    val possibleTargets = targetEdges.map(edge => edgesToTileIds(edge))
      .reduce((a, b) => a.intersect(b))
      .diff(placed)
      .filter(id => filter(id))

    if (possibleTargets.size != 1) {
      throw new RuntimeException("Tile does not have exactly one possible target")
    }

    (targetEdges, tilesById(possibleTargets.head))
  }

  private def findTarget(edge: Edge, x: Int, y: Int, grid: Array[Array[Tile]], placed: List[Long], filter: Long => Boolean): (Int, Tile) = {
    val (targetEdges, tile) = findTargets(List(edge), x, y, grid, placed, filter)
    (targetEdges.head, tile)
  }

  private def orientTile(tile: Tile, predicate: Tile => Boolean): Tile = {
    for (t <- List(tile, tile.flip())) {
      for (rotations <- 0 until 4) {
        val rotated = t.rotate(rotations)
        if (predicate(rotated)) {
          return rotated
        }
      }
    }
    throw new RuntimeException(s"Could not orient tile ${tile.id}")
  }

  private def countSeaMonsterPoints(tile: Tile): Int = {
    val size = tile.cells.size
    var seaMonsterPoints = Set[(Int, Int)]()
    for (row <- 0 until size) {
      for (col <- 0 until size) {
        val points = seaMonster.map { case (smRow, smCol) => (row + smRow, col + smCol) }
        val foundSeaMonster = points.forall { case (r, c) =>
          tile.cells.lift(r) match {
            case Some(value) =>
              value.lift(c) match {
                case Some(value) => value
                case None => false
              }
            case None => false
          }
        }

        if (foundSeaMonster) {
          seaMonsterPoints = seaMonsterPoints ++ points
        }
      }
    }
    seaMonsterPoints.size
  }

  def orientAndCountSeaMonsterPoints(tile: Tile): Int = {
    for (t <- List(tile, tile.flip())) {
      for (rotations <- 0 until 4) {
        val rotated = t.rotate(rotations)
        val seaMonsterCount = countSeaMonsterPoints(rotated)
        if (seaMonsterCount > 0) {
          return seaMonsterCount
        }
      }
    }
    0
  }

  def printGrid(grid: List[List[Tile]]): Unit = {
    grid.foreach(gridRow => println("\n" + gridRow.map(tile => tile.id).mkString("  ")))

    val totalRows = grid.head.head.cells.size
    grid.foreach(gridRow =>
      println(
        "\n" + (0 until totalRows)
          .map { tileRowIndex =>
            gridRow
              .map { tile =>
                tile
                  .cells(tileRowIndex)
                  .map {
                    case true => "#"
                    case _ => "."
                  }
                  .mkString("")
              }
              .mkString("  ")
          }
          .mkString("\n")
      )
    )
  }

  override def toString: String = tiles.map(_.toString).mkString("\n\n")
}

object CameraArray {
  def parse(input: String): CameraArray = {
    CameraArray(
      input
        .split("\\n\\s*\\n")
        .toList
        .map(i => Tile.parse(i.trim))
    )
  }
}
