package com.github.jrhenderson1988.adventofcode2020.day02

case class PasswordDatabase(val passwordPolicies: List[PasswordPolicy]) {
  def totalValidSledRentalPasswords(): Int = {
    passwordPolicies.count(_.passwordMatchesSledRentalRules())
  }

  def totalValidTobogganCorporatePasswords(): Int = {
    passwordPolicies.count(_.passwordMatchesTobogganCorporateRules())
  }
}

object PasswordDatabase {
  def parse(lines: List[String]): PasswordDatabase = {
    new PasswordDatabase(lines.map(PasswordPolicy.parse(_)))
  }
}
