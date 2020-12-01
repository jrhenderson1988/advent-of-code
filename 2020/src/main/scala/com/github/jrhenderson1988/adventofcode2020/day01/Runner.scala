package com.github.jrhenderson1988.adventofcode2020.day01

import java.io.File

import com.github.jrhenderson1988.adventofcode2020.{Answer, Day}

class Runner extends Day {
  override def run(path: File): Option[Answer] = {
    val report = ExpenseReport.parse(fileAsLines(path))
    Some(Answer(report.result(2).toString, report.result(3).toString))
  }
}
