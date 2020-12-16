package com.github.jrhenderson1988.adventofcode2020.day16

import org.scalatest.FunSuite

class TicketValidatorTest extends FunSuite {
  val input: String =
    """
      |class: 1-3 or 5-7
      |row: 6-11 or 33-44
      |seat: 13-40 or 45-50
      |
      |your ticket:
      |7,1,14
      |
      |nearby tickets:
      |7,3,47
      |40,4,50
      |55,2,20
      |38,6,12
      |""".stripMargin

  val tv: TicketValidator = TicketValidator.parse(input)

  test("ticketScanningErrorRate") {
    assert(tv.ticketScanningErrorRate() == 71)
  }
}
