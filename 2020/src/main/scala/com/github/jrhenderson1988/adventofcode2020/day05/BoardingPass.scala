package com.github.jrhenderson1988.adventofcode2020.day05

import scala.math.pow

case class BoardingPass(seatIdentifier: String) {
  def seatId(): Int = {
    (row() * 8) + column()
  }

  def row(): Int = {
    positionOf(seatIdentifier.substring(0, 7))
  }

  def column(): Int = {
    positionOf(seatIdentifier.substring(7))
  }

  def positionOf(input: String): Int = {
    input
      .toCharArray
      .foldLeft((0, pow(2, input.size).intValue - 1)) { case ((low, high), ch) =>
        ch match {
          case 'L' | 'F' => (low, (high + low - 1) / 2)
          case 'R' | 'B' => ((low + high + 1) / 2, high)
        }
      }
      ._1
  }
}
