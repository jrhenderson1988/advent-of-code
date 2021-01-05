package com.github.jrhenderson1988.adventofcode2020.day22

case class Player(deck: List[Int]) {
  def calculateScore(): Int = {
    deck.reverse.zipWithIndex.map { case (value, index) => (index + 1) * value }.sum
  }
}

object Player {
  def parse(lines: List[String]): Player = {
    Player(lines.drop(1).map(line => line.trim.toInt))
  }
}
