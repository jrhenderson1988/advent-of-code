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

  def productOfCornerIds(): Long = getCorners.map(_.id).product

  def waterRoughness(): Int = {
    val grid = buildGrid()
    val image = buildImageFromGrid(grid)
    val seaMonsterPointCount = (orientAndCountSeaMonsterPoints(image))
    image.cells.map(row => row.count(cell => cell)).sum - seaMonsterPointCount
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
        grid(y)(x) =
          if (x == 0 && y == 0) { // TOP LEFT
            orientTile(tilesById(corners.head), tile =>
              edgeCounts(tile.getEdge(Edge.LEFT)) == 1 &&
                edgeCounts(tile.getEdge(Edge.TOP)) == 1 &&
                edgeCounts(tile.getEdge(Edge.RIGHT)) > 1 &&
                edgeCounts(tile.getEdge(Edge.BOTTOM)) > 1
            )
          } else if (x == gridSize - 1 && y == 0) { // Top right, look at the tile to the left, ensure it is a corner
            //            val (targetEdge, tile) = findTarget(Edge.LEFT, x, y, grid, placed, id => corners.contains(id))
            //            orientTile(tile, t => t.getEdge(Edge.LEFT) == targetEdge && edgeCounts(tile.getEdge(Edge.BOTTOM)) > 1)
            val targetEdge = grid(y)(x - 1).getOppositeEdge(Edge.RIGHT)
            val possibleTargets = edgesToTileIds(targetEdge).diff(placed).filter(targetId => corners.contains(targetId))
            if (possibleTargets.size != 1) {
              throw new RuntimeException("Top right corner does not have exactly one possible target")
            }
            orientTile(
              tilesById(possibleTargets.head),
              tile => tile.getEdge(Edge.LEFT) == targetEdge && edgeCounts(tile.getEdge(Edge.BOTTOM)) > 1
            )
          } else if (x == gridSize - 1 && y == gridSize - 1) { // Bottom right, look at the tile to the left and above, ensure it is a corner
            val targetLeft = grid(y)(x - 1).getOppositeEdge(Edge.RIGHT)
            val targetAbove = grid(y - 1)(x).getOppositeEdge(Edge.BOTTOM)
            val possibleTargetsLeft = edgesToTileIds(targetLeft).diff(placed)
            val possibleTargetsAbove = edgesToTileIds(targetAbove).diff(placed)
            val possibleTargets = possibleTargetsLeft.intersect(possibleTargetsAbove).filter(targetId => corners.contains(targetId))
            if (possibleTargets.size != 1) {
              throw new RuntimeException("Bottom right corner does not have exactly one possible target")
            }
            orientTile(
              tilesById(possibleTargets.head),
              tile => tile.getEdge(Edge.TOP) == targetAbove && tile.getEdge(Edge.LEFT) == targetLeft
            )
          } else if (x == 0 && y == gridSize - 1) { // Bottom left: Look at the tile above, ensure it is a corner
            //            val (targetEdge, tile) = findTarget(Edge.TOP, x, y, grid, placed, id => corners.contains(id))
            //            orientTile(tile, t => t.getEdge(Edge.TOP) == targetEdge && edgeCounts(tile.getEdge(Edge.RIGHT)) > 1)
            val targetEdge = grid(y - 1)(x).getOppositeEdge(Edge.BOTTOM)
            val possibleTargets = edgesToTileIds(targetEdge).diff(placed).filter(targetId => corners.contains(targetId))
            if (possibleTargets.size != 1) {
              throw new RuntimeException("Bottom left corner does not have exactly one possible target")
            }
            orientTile(
              tilesById(possibleTargets.head),
              tile => tile.getEdge(Edge.TOP) == targetEdge && edgeCounts(tile.getEdge(Edge.RIGHT)) > 1
            )
          }
          else if (y == 0) { // First row, non-corner, only check left,  Get the right side of the tile to the left
            //            val (targetEdge, tile) = findTarget(Edge.LEFT, x, y, grid, placed, _ => true)
            //            orientTileByLeftEdge(tile, targetEdge)
            val targetEdge = grid(y)(x - 1).getOppositeEdge(Edge.RIGHT)
            val possibleTargets = edgesToTileIds(targetEdge).diff(placed)
            if (possibleTargets.size != 1) {
              throw new RuntimeException("Tile in top row does not have exactly one possible target")
            } else {
              orientTile(tilesById(possibleTargets.head), t => t.getEdge(Edge.LEFT) == targetEdge)
            }
          } else if (x == 0) { // Left edge but not top row, check above
            //            val (targetEdge, tile) = findTarget(Edge.TOP, x, y, grid, placed, _ => true)
            //            orientTile(tile, t => t.getEdge(Edge.TOP) == targetEdge)
            val targetEdge = grid(y - 1)(x).getOppositeEdge(Edge.BOTTOM)
            val possibleTargets = edgesToTileIds(targetEdge).diff(placed)
            if (possibleTargets.size != 1) {
              throw new RuntimeException("Tile in left edge does not have exactly one possible target")
            } else {
              orientTile(tilesById(possibleTargets.head), tile => tile.getEdge(Edge.TOP) == targetEdge)
            }
          } else { // All other tiles that are not in top row or left edge
            val targetLeft = grid(y)(x - 1).getOppositeEdge(Edge.RIGHT)
            val targetAbove = grid(y - 1)(x).getOppositeEdge(Edge.BOTTOM)
            val possibleTargetsLeft = edgesToTileIds(targetLeft).diff(placed)
            val possibleTargetsAbove = edgesToTileIds(targetAbove).diff(placed)
            val possibleTargets = possibleTargetsLeft.intersect(possibleTargetsAbove)
            if (possibleTargets.size != 1) {
              throw new RuntimeException(s"Tile does not have exactly one possible target")
            } else {
              orientTile(
                tilesById(possibleTargets.head),
                tile => tile.getEdge(Edge.LEFT) == targetLeft && tile.getEdge(Edge.TOP) == targetAbove
              )
            }
          }
        placed = placed ++ List(grid(y)(x).id)
      }
    }

    grid.map(_.toList).toList
  }

  private def findTarget(edge: Edge, x: Int, y: Int, grid: Array[Array[Tile]], placed: List[Long], filter: Long => Boolean): (Int, Tile) = {
    val (dx, dy) = edge match {
      case Edge.TOP => (0, -1)
      case Edge.RIGHT => (1, 0)
      case Edge.BOTTOM => (0, 1)
      case Edge.LEFT => (-1, 0)
    }

    val targetEdge = grid(y + dy)(x + dx).getOppositeEdge(edge match {
      case Edge.TOP => Edge.BOTTOM
      case Edge.RIGHT => Edge.LEFT
      case Edge.BOTTOM => Edge.TOP
      case Edge.LEFT => Edge.RIGHT
    })

    val possibleTargets = edgesToTileIds(targetEdge).diff(placed).filter(id => filter(id))
    if (possibleTargets.size != 1) {
      throw new RuntimeException("Bottom left corner does not have exactly one possible target")
    }

    (targetEdge, tilesById(possibleTargets.head))
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
