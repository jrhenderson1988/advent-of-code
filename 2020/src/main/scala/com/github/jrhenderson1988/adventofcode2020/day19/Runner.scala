package com.github.jrhenderson1988.adventofcode2020.day19

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val mvA = MessageValidator.parse(fileToString(path))
    val rules = (Map() ++ mvA.rules) + (8 -> Rule.parse("42 | 42 8")) + (11 -> Rule.parse("42 31 | 42 11 31"))
    val mvB = MessageValidator(rules, mvA.messages)

    Some(
      Answer(
        mvA.totalMatches().toString,
        mvB.totalMatches().toString
      )
    )
  }
}
