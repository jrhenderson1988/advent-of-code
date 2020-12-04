package com.github.jrhenderson1988.adventofcode2020.day04

case class Passport(fields: Map[String, String]) {
  val requiredFields: Map[String, (String) => Boolean] = Map(
    ("byr", isByrValid),
    ("iyr", isIyrValid),
    ("eyr", isEyrValid),
    ("hgt", isHgtValid),
    ("hcl", isHclValid),
    ("ecl", isEclValid),
    ("pid", isPidValid)
  )

  def containsRequiredFields(): Boolean = {
    requiredFields.keys.forall(field => fields.contains(field))
  }

  def isValid(): Boolean = {
    containsRequiredFields() && requiredFields.keys.forall(field => requiredFields(field)(fields(field)))
  }

  private def isByrValid(value: String): Boolean = isYear(value) && between(value.toInt, 1920, 2002)

  private def isIyrValid(value: String): Boolean = isYear(value) && between(value.toInt, 2010, 2020)

  private def isEyrValid(value: String): Boolean = isYear(value) && between(value.toInt, 2020, 2030)

  private def isHgtValid(value: String): Boolean = {
    val pattern = "(\\d+)(cm|in)".r
    if (pattern.matches(value)) {
      val matches = pattern.findAllIn(value)
      val number = matches.group(1).toInt
      val unit = matches.group(2)
      unit match {
        case "cm" => between(number, 150, 193)
        case "in" => between(number, 59, 76)
      }
    } else {
      false
    }
  }

  private def isHclValid(value: String): Boolean = "#[a-f0-9]{6}".r.matches(value)

  private def isEclValid(value: String): Boolean = Set("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(value)

  private def isPidValid(value: String): Boolean = "\\d{9}".r.matches(value)

  private def between(value: Int, min: Int, max: Int): Boolean = value >= min && value <= max

  private def isYear(value: String): Boolean = "\\d\\d\\d\\d".r.matches(value)
}

object Passport {
  def parse(lines: String): Passport = {
    Passport(
      lines
        .replace('\n', ' ')
        .split("\\s+")
        .map(item => item.split(':'))
        .map(pair => (pair(0), pair(1)))
        .toMap
    )
  }
}
