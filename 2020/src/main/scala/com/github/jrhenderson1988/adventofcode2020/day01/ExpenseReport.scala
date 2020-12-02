package com.github.jrhenderson1988.adventofcode2020.day01

case class ExpenseReport(expenses: List[Int]) {
  def result(numbers: Int): Int = {
    expenses
      .combinations(numbers)
      .find(rows => rows.sum == 2020)
      .get
      .product
  }
}

object ExpenseReport {
  def parse(input: List[String]): ExpenseReport = {
    new ExpenseReport(input.map(line => line.toInt))
  }
}
