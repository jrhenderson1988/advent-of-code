package com.github.jrhenderson1988.adventofcode2020.day24

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val fr = Floor.parse(fileAsLines(path))

    Some(
      Answer(
        fr.totalInitialBlackTiles().toString,
        fr.totalBlackTilesAfterDays(100).toString,
      )
    )
  }
}
