package com.github.jrhenderson1988.adventofcode2020.day21

import java.io.File
import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val foodList = FoodList.parse(fileAsLines(path))

    Some(
      Answer(
        foodList.countOccurrencesOfNonAllergenicIngredients().toString,
        foodList.canonicalDangerousIngredientList(),
      )
    )
  }
}
