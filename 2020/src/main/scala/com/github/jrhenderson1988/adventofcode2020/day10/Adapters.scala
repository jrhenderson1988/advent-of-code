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

  /**
   * Notes:
   *
   * First and last (outlet and device) never move and are always part of resulting arrangement, so can be ignored.
   * Assume there are no duplicates (first part only showed differences of 1 and 3)
   * An initial assumption of 1 possible arrangement for 0 adapters
   * Given a list of adapters of say [2, 3, 4, 7, 10]
   * - we start by looking at the 2 jolt adapter, adding together the number of arrangements of up to 3 of its
   *   predecessors (by rating, not index in the list i.e. we look at jolts 1, 0 and -1) which results in 1 combination
   * - next is adapter 3 and its predecessors: jolts 2, 1 & 0 which results in a value of 2 (jolt 0 = 1 + jolt 2 = 1)
   * - next is adapter 4 and its predecessors: jolts 3, 2 & 1 which results in a value of 3 (jolt 2 = 1 + jolt 3 = 2)
   * - next is adapter 7 and its predecessors: jolts 6, 5 & 4 which results in a value of 3 (jolt 4 = 3)
   * - next is adapter 10 and its predecessors: jolts 9, 8 & 7 which results in a value of 3 (jolt 7 = 3)
   *
   * finally, we take the total arrangements of the highest key in the memoized map, in this case 7 which results in a
   * value of 3 arrangements.
   * @return
   */
  def totalPossibleArrangements(): Long = {
    val arrangements = ratings.foldLeft(Map((0L, 1L))) { (memo, r) =>
      memo + (r -> (memo.getOrElse(r - 3L, 0L) + memo.getOrElse(r - 2L, 0L) + memo.getOrElse(r - 1L, 0L)))
    }

    arrangements(arrangements.keySet.max)
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