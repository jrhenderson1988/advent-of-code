package com.github.jrhenderson1988.adventofcode2020.day08

sealed abstract class Instruction
case class JMP(value: Int) extends Instruction
case class ACC(value: Int) extends Instruction
case class NOP(value: Int) extends Instruction
