package com.github.jrhenderson1988.adventofcode2020.day19

sealed trait Rule

case class Terminal(value: String) extends Rule

case class NonTerminal(rules: List[List[Int]]) extends Rule

object Rule {
  def parse(line: String): Rule = {
    val trimmed = line.trim
    if (trimmed.contains("|")) {
      NonTerminal(
        trimmed.split("\\|")
          .toList
          .map { sub =>
            sub.trim
              .split(" ")
              .toList
              .map(_.trim)
              .filter(_.nonEmpty)
              .map(_.toInt)
          }
      )
    } else if (trimmed.contains("\"")) {
      Terminal(trimmed.stripPrefix("\"").stripSuffix("\""))
    } else {
      NonTerminal(List(trimmed.split(" ").toList.map(_.toInt)))
    }
  }
}
