package com.github.jrhenderson1988.adventofcode2020.day19

case class MessageValidator(rules: Map[Int, Rule], messages: List[String]) {
  var dp: Map[(Int, Int, Int), Boolean] = Map[(Int, Int, Int), Boolean]()

  def totalMatches(): Int = messages.count(matches)

  def matches(message: String): Boolean = {
    MessageValidator(rules, List()).matches(message, 0, message.length, 0, 0)
  }

  private def matches(line: String, start: Int, end: Int, rule: Int, l: Int): Boolean = {
    val key = (start, end, rule)
    if (dp.contains(key)) {
      return dp(key)
    }

    var ret = false
    rules(rule) match {
      case Terminal(value) =>
        ret = line.substring(start, end).equals(value)
      case NonTerminal(rules) =>
        for (option <- rules) {
          if (matchList(line, start, end, option, l + 1)) {
            ret = true
          }
        }
    }

    dp += (key -> ret)
    ret
  }

  private def matchList(line: String, start: Int, end: Int, rules: List[Int], l: Int): Boolean = {
    val retVal = if (start == end) {
      rules.isEmpty
    } else if (rules.isEmpty) {
      false
    } else {
      var ret = false
      for (i <- start + 1 to end) {
        if (matches(line, start, i, rules.head, l + 1) && matchList(line, i, end, rules.drop(1), l + 1)) {
          ret = true
        }
      }
      ret
    }

    retVal
  }
}

object MessageValidator {
  def parse(input: String): MessageValidator = {
    val parts = input.split("\\n\\s*\\n").toList

    MessageValidator(
      parseRules(parts.head.split("\\n").toList),
      parts.last.split("\\n").toList.map(_.trim)
    )
  }

  private def parseRules(lines: List[String]): Map[Int, Rule] = {
    lines
      .map { line =>
        val parts = line.split(":\\s?").toList
        (parts.head.toInt, Rule.parse(parts.last))
      }
      .toMap
  }
}
