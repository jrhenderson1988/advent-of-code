package com.github.jrhenderson1988.adventofcode2020.day07

import org.scalatest.FunSuite

class LuggageTest extends FunSuite {
  val inputA =
    """
      |light red bags contain 1 bright white bag, 2 muted yellow bags.
      |dark orange bags contain 3 bright white bags, 4 muted yellow bags.
      |bright white bags contain 1 shiny gold bag.
      |muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
      |shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
      |dark olive bags contain 3 faded blue bags, 4 dotted black bags.
      |vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
      |faded blue bags contain no other bags.
      |dotted black bags contain no other bags.
      |""".stripMargin

  val inputB =
    """
      |shiny gold bags contain 2 dark red bags.
      |dark red bags contain 2 dark orange bags.
      |dark orange bags contain 2 dark yellow bags.
      |dark yellow bags contain 2 dark green bags.
      |dark green bags contain 2 dark blue bags.
      |dark blue bags contain 2 dark violet bags.
      |dark violet bags contain no other bags.
      |""".stripMargin

  val luggageA = Luggage.parse(inputA.trim.split(System.lineSeparator()).toList)
  val luggageB = Luggage.parse(inputB.trim.split(System.lineSeparator()).toList)

  test("parse") {
    assert(
      luggageA ==
        Luggage(
          Map(
            ("light red", Map(("bright white", 1), ("muted yellow", 2))),
            ("dark orange", Map(("bright white", 3), ("muted yellow", 4))),
            ("bright white", Map(("shiny gold", 1))),
            ("muted yellow", Map(("shiny gold", 2), ("faded blue", 9))),
            ("shiny gold", Map(("dark olive", 1), ("vibrant plum", 2))),
            ("dark olive", Map(("faded blue", 3), ("dotted black", 4))),
            ("vibrant plum", Map(("faded blue", 5), ("dotted black", 6))),
            ("faded blue", Map()),
            ("dotted black", Map()),
          )
        )
    )
  }

  test("totalBagsWhichCanCarry") {
    assert(luggageA.totalBagsWhichCanCarry("shiny gold") == 4)
  }

  test("totalBagsRequiredInside") {
    assert(luggageA.totalBagsRequiredInside("shiny gold", 1) == 32)
    assert(luggageB.totalBagsRequiredInside("shiny gold", 1) == 126)
  }
}
