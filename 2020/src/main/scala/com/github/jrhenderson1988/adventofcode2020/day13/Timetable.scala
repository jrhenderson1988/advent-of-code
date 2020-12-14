package com.github.jrhenderson1988.adventofcode2020.day13

case class Timetable(start: Int, ids: List[Int]) {
  def earliestBusTimesWaitTime(): Int = {
    val earliest = ids.filter { item => item != -1 }
      .map { id => (id, (start / id) * id + (if (start % id == 0) 0 else id)) }
      .minBy { item => item._2 }

    (earliest._2 - start) * earliest._1
  }


  /**
   * Yeah, this one was tough. I don't know Chinese Remainder Theorem that was suggested and didn't want to spend a
   * significant portion of time learning it for the purpose of solving this puzzle. Instead, I took to the solutions
   * page of Reddit and found this super-nice solution, written in Python on which I based my approach:
   *
   * https://www.reddit.com/r/adventofcode/comments/kc4njx/2020_day_13_solutions/gfncyoc
   */
  def earliestTimestampWhereBusesDepartSequentially(): Long = {
    var n: Long = 0L
    var step: Long = 1L
    val buses = ids.zipWithIndex.filter { case (id, _) => id != -1 }.map { case (id, index) => (index, id) }
    for ((i, b) <- buses) {
      n = next(n, step, i.toLong, b.toLong)
      step *= b
    }
    n
  }

  // Wrote this because I couldn't get LazyList to work with Longs.
  def next(n: Long, step: Long, index: Long, bus: Long): Long = {
    var c = n
    while ((c + index) % bus != 0) {
      c += step
    }
    c
  }
}

object Timetable {
  def parse(lines: List[String]): Timetable = {
    Timetable(
      lines.head.trim.toInt,
      lines(1).trim.split(",").map(item => if (item == "x") -1 else item.toInt).toList
    )
  }
}
