package com.github.jrhenderson1988.adventofcode2020.day16

case class Rule(ranges: List[Range]) {
  def valid(value: Int): Boolean = {
    ranges.exists { range => range.contains(value) }
  }
}

object Rule {
  def parse(line: String): Rule = {
    Rule(
      line
        .split("or")
        .map(item => {
          val numbers = item.trim.split("-").map(number => number.trim.toInt).toList
          Range.inclusive(numbers.head, numbers.last)
        })
        .toList
    )
  }
}
