package com.github.jrhenderson1988.adventofcode2020.day23

import org.scalatest.FunSuite

class GameTest extends FunSuite {
  test("parse") {
    assert(CrabGame.parse("389125467").cups == new CrabGame(List(3, 8, 9, 1, 2, 5, 4, 6, 7)).cups)
    assert(CrabGame.parse("389125467", 12).cups == new CrabGame(List(3, 8, 9, 1, 2, 5, 4, 6, 7, 10, 11, 12)).cups)
  }

  test("arrangementAfterRounds") {
    assert(new CrabGame(List(3, 8, 9, 1, 2, 5, 4, 6, 7)).arrangementAfterRounds(10) == "92658374")
    assert(new CrabGame(List(3, 8, 9, 1, 2, 5, 4, 6, 7)).arrangementAfterRounds(100) == "67384529")
  }

  test("productOfStarHidingPlaces(10_000_000)") {
    assert(CrabGame.parse("389125467", 1_000_000).productOfStarHidingPlaces(10_000_000) == 149245887792L)
  }
}
