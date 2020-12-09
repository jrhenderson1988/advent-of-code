package com.github.jrhenderson1988.adventofcode2020.day09

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val cipher = XmasCipher.parse(fileAsLines(path))

    Some(
      Answer(
        cipher.findFirstWeakness(25, 25).toString,
        cipher.findEncryptionWeakness(25, 25).toString
      )
    )
  }
}
