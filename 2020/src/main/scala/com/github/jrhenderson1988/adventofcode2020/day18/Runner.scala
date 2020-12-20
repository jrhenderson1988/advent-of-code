package com.github.jrhenderson1988.adventofcode2020.day18

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val homework = Homework.parse(fileAsLines(path))

    Some(
      Answer(
        homework.sumOfExpressionsLeftToRight().toString,
        homework.sumOfExpressionsWithPrecedence().toString
      )
    )
  }
}
