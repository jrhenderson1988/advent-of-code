package com.github.jrhenderson1988.adventofcode2020.day10

case class Adapters(adapterRatings: List[Long]) {
  val ratings: List[Long] = adapterRatings.sorted
  val deviceRating: Long = ratings.max + 3L
  val outletRating: Long = 0L

  def findChainAndCalculateJoltageDifference(): Long = {
    val ratings = allRatings()
    val differences = (0 until ratings.size - 1).map(i => ratings(i + 1) - ratings(i))

    differences.count(_ == 1) * differences.count(_ == 3)
  }

  def totalPossibleArrangements(): Long = {
    val folded = ratings.foldLeft(Map((0L, 1L))) { (paths, jolt) =>
      val result = paths.getOrElse(jolt - 3L, 0L) + paths.getOrElse(jolt - 2L, 0L) + paths.getOrElse(jolt - 1L, 0L)
      paths + (jolt -> result)
    }

    folded(folded.keySet.max)
  }

  private def allRatings(): List[Long] = {
    List(outletRating) ++ ratings ++ List(deviceRating)
  }
}

object Adapters {
  def parse(lines: List[String]): Adapters = {
    Adapters(lines.map(_.trim.toLong))
  }
}