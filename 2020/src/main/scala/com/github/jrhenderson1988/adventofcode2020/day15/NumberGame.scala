package com.github.jrhenderson1988.adventofcode2020.day15

case class NumberGame(input: List[Int]) {
  def nthNumber(n: Int): Int = {
    var spoken = Map[Int, Int]()
    var lastSpoken = input.head
    for (turn <- 1 until n) {
      val number = if (turn < input.size) {
        input(turn)
      } else {
        spoken.get(lastSpoken) match {
          case Some(value) => turn - value
          case None => 0
        }
      }

      spoken += (lastSpoken -> turn)
      lastSpoken = number
    }

    lastSpoken
  }
}

object NumberGame {
  def parse(input: String): NumberGame = {
    NumberGame(input.split(",").map(_.trim.toInt).toList)
  }
}
