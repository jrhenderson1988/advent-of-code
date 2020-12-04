package com.github.jrhenderson1988.adventofcode2020.day04

case class PassportList(passports: List[Passport]) {
  def totalPassportsWithAllRequiredFields(): Int = {
    passports.count(_.containsRequiredFields())
  }

  def totalValidPassports(): Int = {
    passports.count(_.isValid())
  }
}

object PassportList {
  def parse(input: String): PassportList = {
    PassportList(input.split("\\n\\s*\\n").map(_.trim).map(Passport.parse(_)).toList)
  }
}
