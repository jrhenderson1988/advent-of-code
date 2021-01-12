package com.github.jrhenderson1988.adventofcode2020.day25

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    Some(
      Answer(
        Handshake.parse(fileAsLines(path)).encryptionKey().toString,
        "There's no part 2!"
      )
    )
  }
}
