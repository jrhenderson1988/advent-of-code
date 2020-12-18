package com.github.jrhenderson1988.adventofcode2020.day17

import org.scalatest.FunSuite

class PocketDimensionTest extends FunSuite {
  val input = ".#.\n..#\n###"
  val pd3: PocketDimension = PocketDimension.parse(input.split("\\n").toList, 3)
  val pd4: PocketDimension = PocketDimension.parse(input.split("\\n").toList, 4)

  test("totalActiveAfterCycles") {
    assert(pd3.totalActiveAfterCycles(6) == 112)
    assert(pd4.totalActiveAfterCycles(6) == 848)
  }
}
