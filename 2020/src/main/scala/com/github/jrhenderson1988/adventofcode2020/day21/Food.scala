package com.github.jrhenderson1988.adventofcode2020.day21

case class Food(ingredients: Set[String], allergens: Set[String])

object Food {
  def parse(line: String): Food = {
    val allergenListStart = line.indexOf("(contains")

    Food(
      line.substring(0, if (allergenListStart > -1) allergenListStart else line.length)
        .split(" ")
        .map(_.trim)
        .toSet,
      if (allergenListStart > -1) {
        line.substring(allergenListStart + 10, line.indexOf(")"))
          .split(",")
          .map(_.trim)
          .toSet
      } else {
        Set()
      }
    )
  }
}
