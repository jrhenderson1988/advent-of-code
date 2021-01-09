package com.github.jrhenderson1988.adventofcode2020.day24

import com.github.jrhenderson1988.adventofcode2020.day24.Floor.{follow, neighboursOf}

case class Floor(instructions: List[List[Direction]], blackTiles: Set[(Int, Int, Int)]) {
  def initialize(): Floor = {
    val blackTiles = instructions.foldLeft(Set[(Int, Int, Int)]()) { (flipped, instruction) =>
      val tile = follow(instruction)
      if (flipped.contains(tile)) {
        flipped.diff(Set(tile))
      } else {
        flipped.union(Set(tile))
      }
    }

    Floor(instructions, blackTiles)
  }

  def totalInitialBlackTiles(): Int = {
    initialize().blackTiles.size
  }

  def totalBlackTilesAfterDays(days: Int): Int = {
    (0 until days).foldLeft(initialize()) { (floor, _) => floor.cycle() }.blackTiles.size
  }

  def cycle(): Floor = {
    Floor(
      instructions,
      blackTiles
        .flatMap(tile => Set(tile) ++ neighboursOf(tile))
        .filter { tile =>
          val adjacentBlackTiles = neighboursOf(tile).intersect(blackTiles).size
          if (blackTiles.contains(tile)) {
            !(adjacentBlackTiles == 0 || adjacentBlackTiles > 2)
          } else {
            adjacentBlackTiles == 2
          }
        }
    )
  }
}

object Floor {
  def parse(lines: List[String]): Floor = {
    Floor(lines.map { line => parseLine(line) }, Set())
  }

  def parseLine(str: String): List[Direction] = {
    var directions = List[Direction]()
    val line = str.trim

    var i = 0
    while (i < str.length) {
      line(i) match {
        case 'e' => directions = directions ++ List(E())
        case 'w' => directions = directions ++ List(W())
        case 'n' =>
          line(i + 1) match {
            case 'e' => directions = directions ++ List(NE())
            case 'w' => directions = directions ++ List(NW())
            case _ => throw new RuntimeException
          }
          i += 1
        case 's' =>
          line(i + 1) match {
            case 'e' => directions = directions ++ List(SE())
            case 'w' => directions = directions ++ List(SW())
            case _ => throw new RuntimeException
          }
          i += 1
        case _ => throw new RuntimeException
      }
      i += 1
    }

    directions
  }

  def follow(instruction: List[Direction]): (Int, Int, Int) =
    instruction.foldLeft((0, 0, 0)) { (coord, direction) => direction.apply(coord) }

  def neighboursOf(tile: (Int, Int, Int)): Set[(Int, Int, Int)] = {
    Set(E(), NE(), NW(), W(), SW(), SE()).map(direction => direction.apply(tile))
  }
}
