package com.github.jrhenderson1988.adventofcode2020.day01

import org.scalatest.FunSuite

class ExpenseReportTest extends FunSuite {
  val er: ExpenseReport = ExpenseReport(List(1721, 979, 366, 299, 675, 1456))

  test("ExpenseReport.parse") {
    assert(ExpenseReport.parse(List("1721", "979", "366", "299", "675", "1456")) == er)
  }

  test("ExpenseReport.result(2)") {
    assert(er.result(2) == 514579)
  }

  test("ExpenseReport.result(3)") {
    assert(er.result(3) == 241861950)
  }
}
