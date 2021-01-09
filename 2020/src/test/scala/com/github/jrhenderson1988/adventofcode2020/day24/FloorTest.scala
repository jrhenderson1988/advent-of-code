package com.github.jrhenderson1988.adventofcode2020.day24

import org.scalatest.FunSuite

class FloorTest extends FunSuite {
  val input: String =
    """
      |sesenwnenenewseeswwswswwnenewsewsw
      |neeenesenwnwwswnenewnwwsewnenwseswesw
      |seswneswswsenwwnwse
      |nwnwneseeswswnenewneswwnewseswneseene
      |swweswneswnenwsewnwneneseenw
      |eesenwseswswnenwswnwnwsewwnwsene
      |sewnenenenesenwsewnenwwwse
      |wenwwweseeeweswwwnwwe
      |wsweesenenewnwwnwsenewsenwwsesesenwne
      |neeswseenwwswnwswswnw
      |nenwswwsewswnenenewsenwsenwnesesenew
      |enewnwewneswsewnwswenweswnenwsenwsw
      |sweneswneswneneenwnewenewwneswswnese
      |swwesenesewenwneswnwwneseswwne
      |enesenwswwswneneswsenwnewswseenwsese
      |wnwnesenesenenwwnenwsewesewsesesew
      |nenewswnwewswnenesenwnesewesw
      |eneswnwswnwsenenwnwnwwseeswneewsenese
      |neswnwewnwnwseenwseesewsenwsweewe
      |wseweeenwnesenwwwswnew
      |""".stripMargin.trim
  val fr: Floor = Floor.parse(input.linesIterator.toList)

  test("parseLine") {
    assert(Floor.parseLine("esenee") == List(E(), SE(), NE(), E()))
    assert(Floor.parseLine("nwwswee") == List(NW(), W(), SW(), E(), E()))
  }


  test("follow") {
    assert(Floor.follow(List(NW(), W(), SW(), E(), E())) == (0, 0, 0))
  }

  test("totalBlackTiles") {
    assert(fr.totalInitialBlackTiles() == 10)
  }

  test("totalBlackTilesAfterDays") {
    assert(fr.totalBlackTilesAfterDays(1) == 15)
    assert(fr.totalBlackTilesAfterDays(2) == 12)
    assert(fr.totalBlackTilesAfterDays(3) == 25)
    assert(fr.totalBlackTilesAfterDays(4) == 14)
    assert(fr.totalBlackTilesAfterDays(5) == 23)
    assert(fr.totalBlackTilesAfterDays(6) == 28)
    assert(fr.totalBlackTilesAfterDays(7) == 41)
    assert(fr.totalBlackTilesAfterDays(8) == 37)
    assert(fr.totalBlackTilesAfterDays(9) == 49)
    assert(fr.totalBlackTilesAfterDays(10) == 37)

    assert(fr.totalBlackTilesAfterDays(20) == 132)
    assert(fr.totalBlackTilesAfterDays(30) == 259)
    assert(fr.totalBlackTilesAfterDays(40) == 406)
    assert(fr.totalBlackTilesAfterDays(50) == 566)
    assert(fr.totalBlackTilesAfterDays(60) == 788)
    assert(fr.totalBlackTilesAfterDays(70) == 1106)
    assert(fr.totalBlackTilesAfterDays(80) == 1373)
    assert(fr.totalBlackTilesAfterDays(90) == 1844)
    assert(fr.totalBlackTilesAfterDays(100) == 2208)
  }
}
