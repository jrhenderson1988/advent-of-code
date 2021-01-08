package com.github.jrhenderson1988.adventofcode2020.day23

import org.scalatest.FunSuite

class GameTest extends FunSuite {
  test("parse") {
    assert(Game.parse("389125467") == Game(0, List(3, 8, 9, 1, 2, 5, 4, 6, 7)))
  }

  test("round") {
    assert(Game(0, List(3, 8, 9, 1, 2, 5, 4, 6, 7)).round() == Game(1, List(3, 2, 8, 9, 1, 5, 4, 6, 7)))
    assert(Game(1, List(3, 2, 8, 9, 1, 5, 4, 6, 7)).round() == Game(2, List(3, 2, 5, 4, 6, 7, 8, 9, 1)))
    assert(Game(2, List(3, 2, 5, 4, 6, 7, 8, 9, 1)).round() == Game(3, List(7, 2, 5, 8, 9, 1, 3, 4, 6)))
    assert(Game(3, List(7, 2, 5, 8, 9, 1, 3, 4, 6)).round() == Game(4, List(3, 2, 5, 8, 4, 6, 7, 9, 1)))
    assert(Game(4, List(3, 2, 5, 8, 4, 6, 7, 9, 1)).round() == Game(5, List(9, 2, 5, 8, 4, 1, 3, 6, 7)))
    assert(Game(5, List(9, 2, 5, 8, 4, 1, 3, 6, 7)).round() == Game(6, List(7, 2, 5, 8, 4, 1, 9, 3, 6)))
    assert(Game(6, List(7, 2, 5, 8, 4, 1, 9, 3, 6)).round() == Game(7, List(8, 3, 6, 7, 4, 1, 9, 2, 5)))
    assert(Game(7, List(8, 3, 6, 7, 4, 1, 9, 2, 5)).round() == Game(8, List(7, 4, 1, 5, 8, 3, 9, 2, 6)))
    assert(Game(8, List(7, 4, 1, 5, 8, 3, 9, 2, 6)).round() == Game(0, List(5, 7, 4, 1, 8, 3, 9, 2, 6)))
  }

  test("result") {
    assert(Game(0, List(5, 8, 3, 7, 4, 1, 9, 2, 6)).result() == 92658374)
  }

  test("play") {
    assert(Game(0, List(3, 8, 9, 1, 2, 5, 4, 6, 7)).play(10) == 92658374)
    assert(Game(0, List(3, 8, 9, 1, 2, 5, 4, 6, 7)).play(100) == 67384529)
  }
}
