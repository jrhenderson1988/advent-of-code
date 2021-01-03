package com.github.jrhenderson1988.adventofcode2020.day20

case class CameraArray(tiles: List[Tile]) {
  def corners(): List[Tile] = {
    tiles.filter { tile =>
      val others = tiles.diff(List(tile))
      List(tile, Tile(tile.id, tile.cells.map(_.reverse)))
        .exists { tile =>
          tile
            .edges
            .count { edge => others.exists(other => other.hasMatchingEdgeFor(edge)) } == 2
        }
    }
  }

  def productOfCornerIds(): Long = corners().map(_.id).product

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
