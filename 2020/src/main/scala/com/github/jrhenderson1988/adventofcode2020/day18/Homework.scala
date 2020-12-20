package com.github.jrhenderson1988.adventofcode2020.day18

case class Homework(list: List[Expression]) {
  def sumOfExpressionsLeftToRight(): Long = {
    list.map { expression => expression.evaluateLeftToRight().get }.sum
  }

  def sumOfExpressionsWithPrecedence(): Long = {
    list.map { expression => expression.evaluateWithPrecedence().get }.sum
  }
}

object Homework {
  def parse(lines: List[String]): Homework = {
    Homework(lines.map(line => Expression.parse(line)))
  }
}
