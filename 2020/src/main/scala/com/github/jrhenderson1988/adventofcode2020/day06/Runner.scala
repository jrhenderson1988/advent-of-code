package com.github.jrhenderson1988.adventofcode2020.day06

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val responses = GroupResponses.parse(fileToString(path))

    Some(
      Answer(
        responses.totalAnyoneAnsweredYes().toString,
        responses.totalEveryoneAnsweredYes().toString
      )
    )
  }
}
