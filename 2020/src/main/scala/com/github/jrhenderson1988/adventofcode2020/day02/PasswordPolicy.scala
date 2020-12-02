package com.github.jrhenderson1988.adventofcode2020.day02

import scala.util.matching.Regex

case class PasswordPolicy(val first: Int, val second: Int, val letter: Char, val password: String) {
  def passwordMatchesSledRentalRules(): Boolean = {
    val count = password.count(_ == letter)
    count >= first && count <= second
  }

  def passwordMatchesTobogganCorporateRules(): Boolean = {
    List(password.charAt(first - 1) == letter, password.charAt(second - 1) == letter).count(_ == true) == 1
  }

  override def toString: String = s"PasswordPolicy[$first, $second, $letter, $password]"
}

object PasswordPolicy {
  def parse(line: String): PasswordPolicy = {
    val pattern: Regex = "^\\s*(\\d+)-(\\d+)\\s+([a-zA-Z])\\s*:\\s*([a-zA-Z]+)\\s*$".r
    pattern.findFirstMatchIn(line) match {
      case Some(m) => {
        val first = m.group(1).toInt
        val second = m.group(2).toInt
        val letter = m.group(3).charAt(0)
        val password = m.group(4)
        new PasswordPolicy(first, second, letter, password)
      }
      case None => throw new RuntimeException(s"Could not parse password policy: '$line'")
    }
  }
}
