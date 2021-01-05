package com.github.jrhenderson1988.adventofcode2020.day22

import org.scalatest.FunSuite

class PlayerTest extends FunSuite {
  test("parse") {
    assert(
      Player(List(9, 2, 6, 3, 1)) ==
        Player.parse(
          """
            |Player 1:
            |9
            |2
            |6
            |3
            |1
            |""".stripMargin.trim.linesIterator.toList
        )
    )
  }

  test("calculateScore") {
    assert(Player(List(3, 2, 10, 6, 8, 5, 9, 4, 7, 1)).calculateScore() == 306)
  }
}
