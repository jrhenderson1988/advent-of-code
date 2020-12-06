package com.github.jrhenderson1988.adventofcode2020.day06

case class GroupResponses(groups: List[List[Set[Char]]]) {
  def totalAnyoneAnsweredYes(): Int = {
    groups.map(group => group.flatten.toSet.size).sum
  }

  def totalEveryoneAnsweredYes(): Int = {
    groups.map(group => group.reduce((a, b) => a.intersect(b)).size).sum
  }
}

object GroupResponses {
  def parse(input: String): GroupResponses = {
    GroupResponses(
      input.split("\\n\\s*\\n")
        .map(
          group =>
            group
              .trim
              .split("\\n")
              .map(person => person.trim.toCharArray.toSet)
              .toList
        )
        .toList)
  }
}
