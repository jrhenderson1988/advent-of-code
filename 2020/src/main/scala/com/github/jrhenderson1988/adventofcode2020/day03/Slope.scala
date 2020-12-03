package com.github.jrhenderson1988.adventofcode2020.day03

case class Slope(val levels: List[Set[Int]], val visibleLength: Int) {
  def countTreesForSlope(x: Int, y: Int): Int = {
    levels
      .zipWithIndex
      .filter { case (_, row) => row != 0 && row % y == 0 }
      .count { case (trees, row) => trees.contains(((row / y) * x) % visibleLength) }
  }

  def countTreesForSlopes(slopes: List[(Int, Int)]): Int = {
    slopes
      .map { case (x, y) => countTreesForSlope(x, y) }
      .product
  }

  override def toString: String = levels.toString()
}


object Slope {
  def parse(lines: List[String]): Slope = {
    Slope(lines.map(parseLine), lines(0).size)
  }

  def parseLine(line: String): Set[Int] = {
    line
      .toList
      .toArray
      .zipWithIndex
      .filter { case (ch, _) => ch == '#' }
      .map { case (_, i) => i }
      .toSet
  }
}