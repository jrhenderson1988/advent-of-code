package com.github.jrhenderson1988.adventofcode2020.day10

import org.scalatest.FunSuite

class AdaptersTest extends FunSuite {
  val inputA: String = "16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4"
  val inputB: String =
    "28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19" +
      "\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3"
  val adaptersA: Adapters = Adapters.parse(inputA.split("\\n").toList)
  val adaptersB: Adapters = Adapters.parse(inputB.split("\\n").toList)

  test("parse") {
    assert(adaptersA == Adapters(List(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4)))
    assert(adaptersB ==
      Adapters(
        List(
          28, 33, 18, 42, 31, 14, 46, 20,
          48, 47, 24, 23, 49, 45, 19, 38,
          39, 11, 1, 32, 25, 35, 8, 17,
          7, 9, 4, 2, 34, 10, 3
        )
      )
    )
  }

  test("findChainAndCalculateJoltageDifference") {
    assert(adaptersA.findChainAndCalculateJoltageDifference() == 35) // 7x1 and 5x3 = 7 * 5 = 35
    assert(adaptersB.findChainAndCalculateJoltageDifference() == 220) // 22x1 and 10x3 = 22 * 10 = 220
  }

  test("totalPossibleArrangements") {
    assert(adaptersA.totalPossibleArrangements() == 8)
    assert(adaptersB.totalPossibleArrangements() == 19208)
  }
}
