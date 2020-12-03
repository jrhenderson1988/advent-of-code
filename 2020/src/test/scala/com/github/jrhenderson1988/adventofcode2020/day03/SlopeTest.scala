package com.github.jrhenderson1988.adventofcode2020.day03

import org.scalatest.FunSuite

class SlopeTest extends FunSuite {
  val slope = Slope(
    List(
      Set(2, 3),
      Set(0, 4, 8),
      Set(1, 6, 9),
      Set(2, 4, 8, 10),
      Set(1, 5, 6, 9),
      Set(2, 4, 5),
      Set(1, 3, 5, 10),
      Set(1, 10),
      Set(0, 2, 3, 7),
      Set(0, 4, 5, 10),
      Set(1, 4, 8, 10)
    ),
    11
  )

  test("Slope.parse") {
    val input = List(
      "..##.......",
      "#...#...#..",
      ".#....#..#.",
      "..#.#...#.#",
      ".#...##..#.",
      "..#.##.....",
      ".#.#.#....#",
      ".#........#",
      "#.##...#...",
      "#...##....#",
      ".#..#...#.#",
    )
    assert(Slope.parse(input) == slope)
  }

  test("Slope.countTreesForSlope") {
    assert(slope.countTreesForSlope(1, 1) == 2)
    assert(slope.countTreesForSlope(3, 1) == 7)
    assert(slope.countTreesForSlope(5, 1) == 3)
    assert(slope.countTreesForSlope(7, 1) == 4)
    assert(slope.countTreesForSlope(1, 2) == 2)
  }

  test("Slope.countTreesForSlopes") {
    assert(slope.countTreesForSlopes(List((1, 1), (3, 1), (5, 1), (7, 1), (1, 2))) == 336)
  }
}
