package com.github.jrhenderson1988.adventofcode2020.day11

sealed trait Cell {
  def symbol: Char
}

case object Occupied extends Cell {
  val symbol = '#'
}

case object Empty extends Cell {
  val symbol = 'L'
}

case object Floor extends Cell {
  val symbol = '.'
}
