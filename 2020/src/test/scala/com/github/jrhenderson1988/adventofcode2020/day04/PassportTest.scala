package com.github.jrhenderson1988.adventofcode2020.day04

import org.scalatest.FunSuite

class PassportTest extends FunSuite {
  test("Passport.parse") {
    val expected = Passport(
      Map(
        ("ecl", "gry"),
        ("pid", "860033327"),
        ("eyr", "2020"),
        ("hcl", "#fffffd"),
        ("byr", "1937"),
        ("iyr", "2017"),
        ("cid", "147"),
        ("hgt", "183cm")
      )
    )
    val actual = Passport.parse("ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\nbyr:1937 iyr:2017 cid:147 hgt:183cm")
    assert(actual == expected)
  }

  test("Passport.containsRequiredFields") {
    assert(Passport.parse("ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\nbyr:1937 iyr:2017 cid:147 hgt:183cm").containsRequiredFields())
    assert(!Passport.parse("iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\nhcl:#cfa07d byr:1929").containsRequiredFields())
    assert(Passport.parse("hcl:#ae17e1 iyr:2013\neyr:2024\necl:brn pid:760753108 byr:1931\nhgt:179cm").containsRequiredFields())
    assert(!Passport.parse("hcl:#cfa07d eyr:2025 pid:166559648\niyr:2011 ecl:brn hgt:59in").containsRequiredFields())
  }

  test("Passport.isValid") {
    assert(!Passport.parse("eyr:1972 cid:100\nhcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926").isValid())
    assert(!Passport.parse("iyr:2019\nhcl:#602927 eyr:1967 hgt:170cm\necl:grn pid:012533040 byr:1946").isValid())
    assert(!Passport.parse("hcl:dab227 iyr:2012\necl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277").isValid())
    assert(!Passport.parse("hgt:59cm ecl:zzz\neyr:2038 hcl:74454a iyr:2023\npid:3556412378 byr:2007").isValid())
    assert(Passport.parse("pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\nhcl:#623a2f").isValid())
    assert(Passport.parse("eyr:2029 ecl:blu cid:129 byr:1989\niyr:2014 pid:896056539 hcl:#a97842 hgt:165cm").isValid())
    assert(Passport.parse("hcl:#888785\nhgt:164cm byr:2001 iyr:2015 cid:88\npid:545766238 ecl:hzl\neyr:2022").isValid())
    assert(Passport.parse("iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719").isValid())
  }
}
