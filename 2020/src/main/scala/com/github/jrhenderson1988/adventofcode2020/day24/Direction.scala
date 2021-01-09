package com.github.jrhenderson1988.adventofcode2020.day24

sealed trait Direction {
  val delta: (Int, Int, Int)

  def apply(coord: (Int, Int, Int)): (Int, Int, Int) = (coord._1 + delta._1, coord._2 + delta._2, coord._3 + delta._3)
}

case class E() extends Direction {
  override val delta: (Int, Int, Int) = (1, -1, 0)
}

case class NE() extends Direction {
  override val delta: (Int, Int, Int) = (1, 0, -1)
}

case class NW() extends Direction {
  override val delta: (Int, Int, Int) = (0, 1, -1)
}

case class W() extends Direction {
  override val delta: (Int, Int, Int) = (-1, 1, 0)
}

case class SW() extends Direction {
  override val delta: (Int, Int, Int) = (-1, 0, 1)
}

case class SE() extends Direction {
  override val delta: (Int, Int, Int) = (0, -1, 1)
}

