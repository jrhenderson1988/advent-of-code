package com.github.jrhenderson1988.adventofcode2020.day15

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val ng = NumberGame.parse(fileToString(path))

    Some(
      Answer(
        ng.nthNumber(2020).toString,
        ng.nthNumber(30000000).toString
      )
    )
  }
}
