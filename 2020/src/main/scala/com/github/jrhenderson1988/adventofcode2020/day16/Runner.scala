package com.github.jrhenderson1988.adventofcode2020.day16

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val tv = TicketValidator.parse(fileToString(path))
    Some(
      Answer(
        tv.ticketScanningErrorRate().toString,
        tv.departureValuesProduct().toString
      )
    )
  }
}
