package com.github.jrhenderson1988.adventofcode2020.day12

sealed trait Instruction
case class Turn(direction: Char, angle: Int) extends Instruction
case class Move(direction: Char, value: Int) extends Instruction
case class Forward(value: Int) extends Instruction

object Instruction {
  def parse(input: String): Instruction = {
    val letter = input.trim.toUpperCase.charAt(0)
    val number = input.trim.substring(1).toInt
    letter match {
      case 'N' => Move('N', number)
      case 'S' => Move('S', number)
      case 'E' => Move('E', number)
      case 'W' => Move('W', number)
      case 'L' => Turn('L', number)
      case 'R' => Turn('R', number)
      case 'F' => Forward(number)
    }
  }
}