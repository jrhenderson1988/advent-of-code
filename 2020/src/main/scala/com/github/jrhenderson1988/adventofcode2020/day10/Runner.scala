package com.github.jrhenderson1988.adventofcode2020.day10

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val adapters = Adapters.parse(fileAsLines(path))

    Some(
      Answer(
        adapters.findChainAndCalculateJoltageDifference().toString,
        adapters.totalPossibleArrangements().toString
      )
    )
  }
}
