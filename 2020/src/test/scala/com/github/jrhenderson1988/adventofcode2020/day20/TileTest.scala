package com.github.jrhenderson1988.adventofcode2020.day20

import org.scalatest.FunSuite

class TileTest extends FunSuite {
  test("edges") {
    val tile = Tile.parse(
      """Tile 2311:
        |..##.#..#.
        |##..#.....
        |#...##..#.
        |####.#...#
        |##.##.###.
        |##...#.###
        |.#.#.#..##
        |..#....#..
        |###...#.#.
        |..###..###""".stripMargin
    )
    assert(tile.edges.contains(210)) // Top - 0011010010 - 210
    assert(tile.edges.contains(89)) // Right - 0001011001 - 89
    assert(tile.edges.contains(924)) // Bottom - 1110011100 - 924
    assert(tile.edges.contains(318)) // Left - 0100111110 - 318
  }

  test("opposites") {
    val tile = Tile.parse(
      """Tile 2311:
        |..##.#..#.
        |##..#.....
        |#...##..#.
        |####.#...#
        |##.##.###.
        |##...#.###
        |.#.#.#..##
        |..#....#..
        |###...#.#.
        |..###..###""".stripMargin
    )
    assert(tile.opposites.contains(300)) // Top - 0100101100 - 300
    assert(tile.opposites.contains(616)) // Right - 1001101000 - 616
    assert(tile.opposites.contains(231)) // Bottom - 0011100111 - 231
    assert(tile.opposites.contains(498)) // Left - 0111110010 - 498
  }

  test("flip") {
    val a = Tile(1, List(List(true, false, true), List(false, true, false), List(true, true, false)))
    assert(a.flip() == Tile(1, List(List(true, false, true), List(false, true, false), List(false, true, true))))
    val b = Tile(2, List(List(false, false, true), List(true, true, false), List(false, true, false)))
    assert(b.flip() == Tile(2, List(List(true, false, false), List(false, true, true), List(false, true, false))))
  }

  test("rotate") {
    val input = Tile(1, List(List(false, true, false), List(true, false, false), List(false, true, true)))
    assert(input.rotate(1) == Tile(1, List(List(false, true, false), List(true, false, true), List(true, false, false))))
    assert(input.rotate(2) == Tile(1, List(List(true, true, false), List(false, false, true), List(false, true, false))))
    assert(input.rotate(3) == Tile(1, List(List(false, false, true), List(true, false, true), List(false, true, false))))
    assert(input.rotate(4) == input)
  }

  test("something") {
    val tile = Tile.parse(
      """Tile 2311:
        |..##.#..#.
        |##..#.....
        |#...##..#.
        |####.#...#
        |##.##.###.
        |##...#.###
        |.#.#.#..##
        |..#....#..
        |###...#.#.
        |..###..###""".stripMargin
    )
    println(tile.id)
    println(tile.edges)
    println(tile.opposites)
  }
}
