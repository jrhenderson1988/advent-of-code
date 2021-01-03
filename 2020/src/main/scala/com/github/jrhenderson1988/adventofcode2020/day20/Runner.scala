package com.github.jrhenderson1988.adventofcode2020.day20

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val ca = CameraArray.parse(fileToString(path))

    Some(
      Answer(
        ca.productOfCornerIds().toString,
        "TODO",
      )
    )
  }
}
