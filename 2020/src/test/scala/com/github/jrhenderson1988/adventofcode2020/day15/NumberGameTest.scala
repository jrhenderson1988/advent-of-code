package com.github.jrhenderson1988.adventofcode2020.day15

import org.scalatest.FunSuite

class NumberGameTest extends FunSuite {
  test("nthNumber 2020") {
    assert(NumberGame(List(0, 3, 6)).nthNumber(2020) == 436)
    assert(NumberGame(List(1, 3, 2)).nthNumber(2020) == 1)
    assert(NumberGame(List(2, 1, 3)).nthNumber(2020) == 10)
    assert(NumberGame(List(1, 2, 3)).nthNumber(2020) == 27)
    assert(NumberGame(List(2, 3, 1)).nthNumber(2020) == 78)
    assert(NumberGame(List(3, 2, 1)).nthNumber(2020) == 438)
    assert(NumberGame(List(3, 1, 2)).nthNumber(2020) == 1836)
  }

  // These tests will take quite a while, code isn't optimised at all.
  test("nthNumber 30000000") {
    assert(NumberGame(List(0,3,6)).nthNumber(30000000) == 175594)
    assert(NumberGame(List(1,3,2)).nthNumber(30000000) == 2578)
    assert(NumberGame(List(2,1,3)).nthNumber(30000000) == 3544142)
    assert(NumberGame(List(1,2,3)).nthNumber(30000000) == 261214)
    assert(NumberGame(List(2,3,1)).nthNumber(30000000) == 6895259)
    assert(NumberGame(List(3,2,1)).nthNumber(30000000) == 18)
    assert(NumberGame(List(3,1,2)).nthNumber(30000000) == 362)
  }
}
