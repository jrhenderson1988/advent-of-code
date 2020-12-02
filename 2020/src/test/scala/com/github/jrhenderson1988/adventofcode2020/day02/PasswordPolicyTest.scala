package com.github.jrhenderson1988.adventofcode2020.day02

import org.scalatest.FunSuite

class PasswordPolicyTest extends FunSuite {
  val a: PasswordPolicy = PasswordPolicy(1, 3, 'a', "abcde")
  val b: PasswordPolicy = PasswordPolicy(1, 3, 'b', "cdefg")
  val c: PasswordPolicy = PasswordPolicy(2, 9, 'c', "ccccccccc")

  test("PasswordPolicy.parse") {
    assert(PasswordPolicy.parse("1-3 a: abcde") == a)
    assert(PasswordPolicy.parse("1-3 b: cdefg") == b)
    assert(PasswordPolicy.parse("2-9 c: ccccccccc") == c)
  }

  test("PasswordPolicy.passwordMatchesSledRentalRules") {
    assert(a.passwordMatchesSledRentalRules())
    assert(!b.passwordMatchesSledRentalRules())
    assert(c.passwordMatchesSledRentalRules())
  }

  test("PasswordPolicy.passwordMatchesTobogganCorporateRules") {
    assert(a.passwordMatchesTobogganCorporateRules())
    assert(!b.passwordMatchesTobogganCorporateRules())
    assert(!c.passwordMatchesTobogganCorporateRules())
  }
}
