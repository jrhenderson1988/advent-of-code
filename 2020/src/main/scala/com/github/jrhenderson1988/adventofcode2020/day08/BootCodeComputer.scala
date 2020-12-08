package com.github.jrhenderson1988.adventofcode2020.day08

class BootCodeComputer(val instructions: List[Instruction]) {
  var pointer = 0
  var accumulator = 0

  def executeUntilInfiniteLoop(): Int = {
    val cpu = new BootCodeComputer(instructions)
    val terminated = cpu.executeAll()
    if (!terminated) {
      cpu.accumulator
    } else {
      -1
    }
  }

  def fixAndExecute(): Int = {
    for (index <- instructions.indices) {
      val cpu: Option[BootCodeComputer] = instructions(index) match {
        case JMP(_) | NOP(_) => {
          Some(
            new BootCodeComputer(
              instructions
                .indices
                .map { i => (i, instructions(i)) }
                .map { case (i, instruction) =>
                  if (i == index) {
                    instruction match {
                      case JMP(value) => NOP(value)
                      case ACC(value) => ACC(value)
                      case NOP(value) => JMP(value)
                    }
                  } else {
                    instruction
                  }
                }
                .toList
            )
          )
        }
        case ACC(_) => None
      }

      cpu match {
        case Some(cpu) => {
          if (cpu.executeAll()) {
            return cpu.accumulator
          }
        }
        case None =>
      }
    }

    -1
  }

  private def execute(instruction: Instruction): Unit = {
    instruction match {
      case JMP(value) => pointer += (value - 1)
      case ACC(value) => accumulator += value
      case NOP(_) =>
    }

    pointer += 1
  }

  // Returns false if we end up in infinite loop
  // Returns true if IP ends up out of bounds
  private def executeAll(): Boolean = {
    var linesExecuted = Set[Int]()
    while (true) {
      if (linesExecuted.contains(pointer)) {
        return false
      } else if (pointer >= instructions.size) {
        return true
      }

      linesExecuted = linesExecuted.union(Set(pointer))
      execute(instructions(pointer))
    }

    true
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[BootCodeComputer]

  override def equals(other: Any): Boolean = other match {
    case that: BootCodeComputer =>
      (that canEqual this) &&
        instructions == that.instructions
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(instructions)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object BootCodeComputer {
  def parse(lines: List[String]): BootCodeComputer = {
    new BootCodeComputer(lines.map(parseLine))
  }

  def parseLine(line: String): Instruction = {
    val parts = line.trim.split(' ')
    if (parts.length != 2) {
      throw new RuntimeException
    }

    val instruction = parts(0).toLowerCase.trim
    val value = parts(1).trim.stripPrefix("+").toInt

    instruction match {
      case "nop" => NOP(value)
      case "acc" => ACC(value)
      case "jmp" => JMP(value)
      case _ => throw new RuntimeException
    }
  }
}