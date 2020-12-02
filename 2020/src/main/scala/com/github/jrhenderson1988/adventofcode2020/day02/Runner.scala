package com.github.jrhenderson1988.adventofcode2020.day02

import java.io.File

import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    Some(
      Answer(
        PasswordDatabase.parse(fileAsLines(path)).totalValidSledRentalPasswords().toString,
        PasswordDatabase.parse(fileAsLines(path)).totalValidTobogganCorporatePasswords().toString
      )
    )
  }
}
