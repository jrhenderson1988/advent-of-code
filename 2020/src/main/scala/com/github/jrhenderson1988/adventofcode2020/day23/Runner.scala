package com.github.jrhenderson1988.adventofcode2020.day23

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val game = Game.parse(fileToString(path))
    Some(
      Answer(
        game.play(100).toString,
        "TODO"
      )
    )
  }
}
