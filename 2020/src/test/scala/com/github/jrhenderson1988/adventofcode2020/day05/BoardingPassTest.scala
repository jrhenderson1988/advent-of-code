package com.github.jrhenderson1988.adventofcode2020.day05

import org.scalatest.FunSuite

class BoardingPassTest extends FunSuite {
  test("BoardingPass.row") {
    assert(BoardingPass("FBFBBFFRLR").row() == 44)
    assert(BoardingPass("BFFFBBFRRR").row() == 70)
    assert(BoardingPass("FFFBBBFRRR").row() == 14)
    assert(BoardingPass("BBFFBBFRLL").row() == 102)
  }

  test("BoardingPass.column") {
    assert(BoardingPass("FBFBBFFRLR").column() == 5)
    assert(BoardingPass("BFFFBBFRRR").column() == 7)
    assert(BoardingPass("FFFBBBFRRR").column() == 7)
    assert(BoardingPass("BBFFBBFRLL").column() == 4)
  }

  test("BoardingPass.seatId") {
    assert(BoardingPass("FBFBBFFRLR").seatId() == 357)
    assert(BoardingPass("BFFFBBFRRR").seatId() == 567)
    assert(BoardingPass("FFFBBBFRRR").seatId() == 119)
    assert(BoardingPass("BBFFBBFRLL").seatId() == 820)
  }

}
