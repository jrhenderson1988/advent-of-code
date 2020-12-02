package com.github.jrhenderson1988.adventofcode2020.day02

import org.scalatest.FunSuite

class PasswordDatabaseTest extends FunSuite {
  val db: PasswordDatabase = PasswordDatabase(
    List(
      PasswordPolicy(1, 3, 'a', "abcde"),
      PasswordPolicy(1, 3, 'b', "cdefg"),
      PasswordPolicy(2, 9, 'c', "ccccccccc")
    )
  )

  test("PasswordDatabase.parse") {
    assert(PasswordDatabase.parse(List("1-3 a: abcde", "1-3 b: cdefg", "2-9 c: ccccccccc")) == db)
  }

  test("PasswordDatabase.totalValidSledRentalPasswords") {
    assert(db.totalValidSledRentalPasswords() == 2)
  }

  test("PasswordDatabase.totalValidTobogganCorporatePasswords()") {
    assert(db.totalValidTobogganCorporatePasswords() == 1)
  }
}
