package com.github.jrhenderson1988.adventofcode2020.day23

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    Some(
      Answer(
        CrabGame.parse(fileToString(path)).arrangementAfterRounds(100),
        CrabGame.parse(fileToString(path), 1_000_000).productOfStarHidingPlaces(10_000_000).toString,
      )
    )
  }
}
