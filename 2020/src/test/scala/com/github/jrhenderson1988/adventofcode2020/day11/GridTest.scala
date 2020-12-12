package com.github.jrhenderson1988.adventofcode2020.day11

import org.scalatest.FunSuite

class GridTest extends FunSuite {
  val input: String =
    """
      |L.LL.LL.LL
      |LLLLLLL.LL
      |L.L.L..L..
      |LLLL.LL.LL
      |L.LL.LL.LL
      |L.LLLLL.LL
      |..L.L.....
      |LLLLLLLLLL
      |L.LLLLLL.L
      |L.LLLLL.LL
      |""".stripMargin.trim

  val grid: Grid = Grid.parse(input.split("\\n").toList)

  test("totalOccupiedAfterGridStabilizesWithImmediateNeighbours") {
    assert(grid.totalOccupiedAfterGridStabilizesWithImmediateNeighbours() == 37)
  }

  test("totalOccupiedAfterGridStabilizesWithDistantNeighbours") {
    assert(grid.totalOccupiedAfterGridStabilizesWithDistantNeighbours() == 26)
  }
}
