package com.github.jrhenderson1988.adventofcode2020.day21

import org.scalatest.FunSuite

class FoodListTest extends FunSuite {
  val input: List[String] =
    """
      |mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
      |trh fvjkl sbzzf mxmxvkd (contains dairy)
      |sqjhc fvjkl (contains soy)
      |sqjhc mxmxvkd sbzzf (contains fish)
      |""".stripMargin.trim.linesIterator.toList
  val foodList: FoodList = FoodList.parse(input)

  test("parse") {
    assert(foodList ==
      FoodList(
        List(
          Food(Set("mxmxvkd", "kfcds", "sqjhc", "nhms"), Set("dairy", "fish")),
          Food(Set("trh", "fvjkl", "sbzzf", "mxmxvkd"), Set("dairy")),
          Food(Set("sqjhc", "fvjkl"), Set("soy")),
          Food(Set("sqjhc", "mxmxvkd", "sbzzf"), Set("fish"))
        )
      )
    )
  }

  test("countOccurrencesOfNonAllergenicIngredients") {
    assert(foodList.countOccurrencesOfNonAllergenicIngredients() == 5)
  }

  test("canonicalDangerousIngredientList") {
    assert(foodList.canonicalDangerousIngredientList() == "mxmxvkd,sqjhc,fvjkl")
  }
}
