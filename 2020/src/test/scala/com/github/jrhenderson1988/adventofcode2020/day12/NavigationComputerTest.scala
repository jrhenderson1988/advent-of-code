package com.github.jrhenderson1988.adventofcode2020.day12

import org.scalatest.FunSuite

class NavigationComputerTest extends FunSuite {
  val input = "F10\nN3\nF7\nR90\nF11"
  val cpu = NavigationComputer.parse(input.split("\\n").toList)

  test("executeInstructionsAgainstShipAndReportDistance") {
    assert(cpu.executeInstructionsAgainstShipAndReportDistance() == 25)
  }

  test("executeInstructionsWithWaypointAndReportDistance") {
    assert(cpu.executeInstructionsWithWaypointAndReportDistance() == 286)
  }
}
