package com.github.jrhenderson1988.adventofcode2020.day22

import org.scalatest.FunSuite

class GameTest extends FunSuite {
  val input: String =
    """
      |Player 1:
      |9
      |2
      |6
      |3
      |1
      |
      |Player 2:
      |5
      |8
      |4
      |7
      |10
      |""".stripMargin.trim
  val game: Game = Game(Player(List(9, 2, 6, 3, 1)), Player(List(5, 8, 4, 7, 10)))

  test("parse") {
    assert(Game.parse(input) == game)
  }

  test("combatWinnersScore") {
    assert(game.combatWinnersScore() == 306)
  }
}
