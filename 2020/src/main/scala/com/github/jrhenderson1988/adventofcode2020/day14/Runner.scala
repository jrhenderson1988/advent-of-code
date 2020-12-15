package com.github.jrhenderson1988.adventofcode2020.day14

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val cpu = Computer.parse(fileAsLines(path))

    Some(
      Answer(
        cpu.sumOfMemoryAfterExecution().toString,
        cpu.sumOfMemoryAfterExecutingV2().toString
      )
    )
  }
}
