package com.github.jrhenderson1988.adventofcode2020.day11

case class Grid(cells: Map[(Int, Int), Cell], width: Int, height: Int) {
  def totalOccupiedAfterGridStabilizesWithImmediateNeighbours(): Int = {
    stabilize(grid => grid.roundWithImmediateNeighbours()).totalOccupied()
  }

  def totalOccupiedAfterGridStabilizesWithDistantNeighbours(): Int = {
    stabilize(grid => grid.roundWithDistantNeighbours()).totalOccupied()
  }

  private def stabilize(roundFn: Grid => Grid): Grid = {
    var grid = this
    while (true) {
      val round = roundFn(grid)
      if (grid == round) {
        return grid
      } else {
        grid = round
      }
    }
    grid
  }

  private def totalOccupied(): Int = {
    cells.values.count(_ == Occupied)
  }

  private def roundWithImmediateNeighbours(): Grid = {
    Grid(
      cells.map { case ((x, y), cell) => (x, y) -> applyRule(cell, immediateNeighboursOf(x, y), 4) },
      width,
      height
    )
  }

  private def roundWithDistantNeighbours(): Grid = {
    Grid(
      cells.map { case ((x, y), cell) => (x, y) -> applyRule(cell, distantNeighboursOf(x, y), 5) },
      width,
      height
    )
  }

  private def deltas(): List[(Int, Int)] = List((0, 1), (1, 0), (0, -1), (-1, 0), (-1, -1), (1, -1), (1, 1), (-1, 1))

  private def immediateNeighboursOf(x: Int, y: Int): List[Cell] = {
    deltas()
      .map { case (xd, yd) => cells.get((x + xd, y + yd)) }
      .filter(_.isDefined)
      .map(_.get)
  }

  private def distantNeighboursOf(x: Int, y: Int): List[Cell] = {
    deltas()
      .map { case (xd, yd) =>
        LazyList.from(1)
          .map { i => cells.get(x + (xd * i), y + (yd * i)) }
          .find { item => item.isEmpty || item.get != Floor }
          .flatten
      }
      .filter { item => item.isDefined }
      .map { item => item.get }
  }

  private def applyRule(cell: Cell, neighbours: List[Cell], threshold: Int): Cell = {
    if (cell == Empty && !neighbours.contains(Occupied)) {
      Occupied
    } else if (cell == Occupied && neighbours.count(_ == Occupied) >= threshold) {
      Empty
    } else {
      cell
    }
  }

  override def toString: String =
    (0 until height)
      .map { y =>
        (0 until width)
          .map { x =>
            cells((x, y)).symbol
          }
          .mkString("")
      }
      .mkString("\n")
}

object Grid {
  def parse(lines: List[String]): Grid = {
    var cells = Map[(Int, Int), Cell]()
    val height = lines.size
    val width = lines.head.trim.length

    lines.zipWithIndex.map { case (line, y) =>
      line.trim.toCharArray.zipWithIndex.map { case (ch, x) =>
        val cell: Cell = ch match {
          case '#' => Occupied
          case 'L' => Empty
          case _ => Floor
        }
        cells = cells + ((x, y) -> cell)
      }
    }

    Grid(cells, width, height)
  }
}
