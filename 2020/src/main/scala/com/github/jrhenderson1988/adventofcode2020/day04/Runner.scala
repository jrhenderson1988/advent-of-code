package com.github.jrhenderson1988.adventofcode2020.day04

import java.io.File

import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val list = PassportList.parse(fileToString(path))
    Some(
      Answer(
        list.totalPassportsWithAllRequiredFields().toString,
        list.totalValidPassports().toString
      )
    )
  }
}
