package com.github.jrhenderson1988.adventofcode2020.day08

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val cpu = BootCodeComputer.parse(fileAsLines(path))

    Some(
      Answer(
        cpu.executeUntilInfiniteLoop().toString,
        cpu.fixAndExecute().toString
      )
    )
  }
}
