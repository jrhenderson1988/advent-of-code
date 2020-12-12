package com.github.jrhenderson1988.adventofcode2020.day11

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val grid = Grid.parse(fileAsLines(path))

    Some(
      Answer(
        grid.totalOccupiedAfterGridStabilizesWithImmediateNeighbours().toString,
        grid.totalOccupiedAfterGridStabilizesWithDistantNeighbours().toString
      )
    )
  }
}
