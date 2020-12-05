package com.github.jrhenderson1988.adventofcode2020.day05

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

import scala.math.pow

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val boardingPasses = BoardingPasses.parse(fileAsLines(path))

    Some(
      Answer(
        boardingPasses.highestSeatId().toString,
        boardingPasses.findMissingSeatId().toString
      )
    )
  }
}
