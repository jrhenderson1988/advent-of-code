package com.github.jrhenderson1988.adventofcode2020.day12

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val cpu = NavigationComputer.parse(fileAsLines(path))

    Some(
      Answer(
        cpu.executeInstructionsAgainstShipAndReportDistance().toString,
        cpu.executeInstructionsWithWaypointAndReportDistance().toString
      )
    )
  }
}
