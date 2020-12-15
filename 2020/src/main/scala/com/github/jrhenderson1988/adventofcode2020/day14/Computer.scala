package com.github.jrhenderson1988.adventofcode2020.day14

import scala.math.pow

case class Computer(instructions: List[Instruction], mask: List[Option[Boolean]], memory: Map[Long, Long]) {
  def sumOfMemoryAfterExecution(): Long = {
    executeInstructionsAndSum(1)
  }

  def sumOfMemoryAfterExecutingV2(): Long = {
    executeInstructionsAndSum(2)
  }

  private def executeInstructionsAndSum(version: Short): Long = {
    executeInstructions(version).memory.values.sum
  }

  def executeInstructions(version: Short): Computer = {
    instructions.foldLeft(this) { (cpu, instruction) => cpu.execute(instruction, version) }
  }

  def execute(instruction: Instruction, version: Short): Computer = {
    instruction match {
      case SetMask(m) => setMask(m)
      case SetMemory(id, value) =>
        if (version == 2) {
          setMemoryV2(id, value)
        } else {
          setMemory(id, value)
        }
    }
  }

  private def setMask(m: List[Option[Boolean]]): Computer = {
    Computer(instructions, m, memory)
  }

  private def setMemory(id: Long, value: Long): Computer = {
    Computer(
      instructions,
      mask,
      memory + (id -> binaryListToLong(applyMaskToBinaryList(longToBinaryList(value))))
    )
  }

  def setMemoryV2(id: Long, value: Long): Computer = {
    val maskedAddress = applyMaskToBinaryListV2(longToBinaryList(id))
    val totalFloating = maskedAddress.count(item => item.isEmpty)

    var newMemory = memory
    val floatingIndices = maskedAddress.zipWithIndex.filter { case (item, _) => item.isEmpty }.map { case (_, index) => index}
    for (i <- 0 until pow(2, totalFloating).toInt) {
      val replacements = i.toBinaryString
        .reverse
        .padTo(totalFloating, '0')
        .reverse
        .toCharArray
        .map(item => if (item == '0') false else true)
        .toList

      val address = floatingIndices
        .zipWithIndex
        .foldLeft(maskedAddress) { case (addr, (index, i)) =>
          addr.updated(index, Some(replacements(i)))
        }
        .map {
          case Some(value) => value
          case None => throw new RuntimeException
        }

      newMemory = newMemory + (binaryListToLong(address) -> value)
    }

    Computer(instructions, mask, newMemory)
  }

  def longToBinaryList(value: Long): List[Boolean] = {
    value
      .toBinaryString
      .reverse
      .padTo(36, '0')
      .reverse
      .toCharArray
      .map {
        case '1' => true
        case '0' => false
      }
      .toList
  }

  def binaryListToLong(list: List[Boolean]): Long = {
    list
      .reverse
      .zipWithIndex
      .map { case (value, index) => (if (value) pow(2, index) else 0).toLong }
      .sum
  }

  private def applyMaskToBinaryList(list: List[Boolean]): List[Boolean] = {
    if (list.size != 36 || mask.size != 36) {
      throw new RuntimeException
    }

    list
      .zipWithIndex
      .map {
        case (bit, index) => mask(index) match {
          case Some(value) => value
          case None => bit
        }
      }
  }

  def applyMaskToBinaryListV2(list: List[Boolean]): List[Option[Boolean]] = {
    if (list.size != 36 || mask.size != 36) {
      throw new RuntimeException
    }

    list
      .zipWithIndex
      .map {
        case (bit, index) => mask(index) match {
          case Some(value) => Some(if (!value) bit else true)
          case None => None
        }
      }
  }
}

object Computer {
  def parse(lines: List[String]): Computer = {
    Computer(lines.map(line => Instruction.parse(line)), List.fill(36)(None), Map())
  }
}
