package com.github.jrhenderson1988.adventofcode2020.day03

import java.io.File

import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val slope = Slope.parse(fileAsLines(path))
    Some(
      Answer(
        slope.countTreesForSlope(3, 1).toString,
        slope.countTreesForSlopes(List((1, 1), (3, 1), (5, 1), (7, 1), (1, 2))).toString
      )
    )
  }
}
