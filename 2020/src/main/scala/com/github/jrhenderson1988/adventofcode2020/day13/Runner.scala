package com.github.jrhenderson1988.adventofcode2020.day13

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val timetable = Timetable.parse(fileAsLines(path))

    Some(
      Answer(
        timetable.earliestBusTimesWaitTime().toString,
        timetable.earliestTimestampWhereBusesDepartSequentially().toString
      )
    )
  }
}
