package com.github.jrhenderson1988.adventofcode2020.day21

import org.scalatest.FunSuite

class FoodTest extends FunSuite {
  val input: String = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)"
  val food: Food = Food.parse(input)

  test("parse") {
    assert(food == Food(Set("mxmxvkd", "kfcds", "sqjhc", "nhms"), Set("dairy", "fish")))
  }
}
