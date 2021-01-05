package com.github.jrhenderson1988.adventofcode2020.day21

case class FoodList(foods: List[Food]) {
  def countOccurrencesOfNonAllergenicIngredients(): Int = {
    val allergens = findAllergens().values.toSet
    val nonAllergens = foods
      .map(_.ingredients)
      .fold(Set()) { case (acc, ingredients) => acc.union(ingredients) }
      .diff(allergens)

    foods.map { food => food.ingredients.intersect(nonAllergens).size }.sum
  }

  def canonicalDangerousIngredientList(): String = {
    findAllergens()
      .toList
      .sortBy(_._1)
      .map(_._2)
      .mkString(",")
  }

  def findAllergens(): Map[String, String] = {
    val allergens = foods.map(_.allergens).fold(Set()) { case (acc, allergens) => acc.union(allergens) }

    var result = allergens
      .map { allergen =>
        allergen -> foods
          .filter { food => food.allergens.contains(allergen) }
          .map { food => food.ingredients }
          .reduce { (acc, ingredients) => acc.intersect(ingredients) }
      }
      .toMap

    while (true) {
      val singleAllergens = result
        .filter { case (_, ingredients) => ingredients.size == 1 }
        .map { case (allergen, _) => allergen }
        .toSet

      if (singleAllergens.size == result.size) {
        return result.map { case (allergen, ingredients) => allergen -> ingredients.head }
      }

      val allergenIngredients = singleAllergens.flatMap(a => result(a))
      result = result.map { case (allergen, ingredients) =>
        if (singleAllergens.contains(allergen)) {
          allergen -> ingredients
        } else {
          allergen -> ingredients.diff(allergenIngredients)
        }
      }
    }

    Map()
  }
}

object FoodList {
  def parse(input: List[String]): FoodList = {
    FoodList(input.map(line => Food.parse(line)))
  }
}