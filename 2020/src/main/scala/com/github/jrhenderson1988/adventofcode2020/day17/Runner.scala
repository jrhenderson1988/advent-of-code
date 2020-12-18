package com.github.jrhenderson1988.adventofcode2020.day17

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    Some(
      Answer(
        PocketDimension.parse(fileAsLines(path), 3).totalActiveAfterCycles(6).toString,
        PocketDimension.parse(fileAsLines(path), 4).totalActiveAfterCycles(6).toString
      )
    )
  }
}
