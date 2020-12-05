package com.github.jrhenderson1988.adventofcode2020.day05

import org.scalatest.FunSuite

class BoardingPassesTest extends FunSuite {
  test("BoardingPasses.parse") {
    val input = List("FBFBBFFRLR", "BFFFBBFRRR", "FFFBBBFRRR", "BBFFBBFRLL")
    val expected = BoardingPasses(
      List(
        BoardingPass("FBFBBFFRLR"),
        BoardingPass("BFFFBBFRRR"),
        BoardingPass("FFFBBBFRRR"),
        BoardingPass("BBFFBBFRLL")
      )
    )
    assert(BoardingPasses.parse(input) == expected)
  }

  test("BoardingPasses.highestSeatId") {
    val passes = BoardingPasses(
      List(
        BoardingPass("FBFBBFFRLR"),
        BoardingPass("BFFFBBFRRR"),
        BoardingPass("FFFBBBFRRR"),
        BoardingPass("BBFFBBFRLL")
      )
    )
    assert(passes.highestSeatId() == 820)
  }
}
