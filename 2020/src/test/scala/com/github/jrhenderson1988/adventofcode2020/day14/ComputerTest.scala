package com.github.jrhenderson1988.adventofcode2020.day14

import org.scalatest.FunSuite

class ComputerTest extends FunSuite {
  val input: String = "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\nmem[8] = 11\nmem[7] = 101\nmem[8] = 0"
  val input2: String = "mask = 000000000000000000000000000000X1001X\nmem[42] = 100\nmask = 00000000000000000000000000000000X0XX\nmem[26] = 1"
  val cpu: Computer = Computer.parse(input.split("\\n").toList)
  val cpu2: Computer = Computer.parse(input2.split("\\n").toList)

  test("sumOfMemoryAfterExecution") {
    assert(cpu.sumOfMemoryAfterExecution() == 165)
  }

  test("sumOfMemoryAfterExecutingV2") {
    assert(cpu2.sumOfMemoryAfterExecutingV2() == 208)
  }
}
