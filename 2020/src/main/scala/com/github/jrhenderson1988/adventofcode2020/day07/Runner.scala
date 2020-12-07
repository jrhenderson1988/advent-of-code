package com.github.jrhenderson1988.adventofcode2020.day07

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val protocol = Luggage.parse(fileAsLines(path))

    Some(
      Answer(
        protocol.totalBagsWhichCanCarry("shiny gold").toString,
        protocol.totalBagsRequiredInside("shiny gold", 1).toString
      )
    )
  }
}
