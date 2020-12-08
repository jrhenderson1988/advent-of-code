package com.github.jrhenderson1988.adventofcode2020.day08

import org.scalatest.FunSuite

class BootCodeComputerTest extends FunSuite {
  val input = "nop +0\nacc +1\njmp +4\nacc +3\njmp -3\nacc -99\nacc +1\njmp -4\nacc +6"
  val cpu = BootCodeComputer.parse(input.split("\\n").toList)

  test("parse") {
    assert(cpu ==
      new BootCodeComputer(List(NOP(0), ACC(1), JMP(4), ACC(3), JMP(-3), ACC(-99), ACC(1), JMP(-4), ACC(6))))
  }

  test("executeUntilInfiniteLoop") {
    assert(cpu.executeUntilInfiniteLoop() == 5)
  }

  test("fixAndExecute") {
    assert(cpu.fixAndExecute() == 8)
  }
}
