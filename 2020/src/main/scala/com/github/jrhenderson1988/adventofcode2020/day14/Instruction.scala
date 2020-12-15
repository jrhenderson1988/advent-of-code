package com.github.jrhenderson1988.adventofcode2020.day14

import scala.util.matching.Regex

sealed trait Instruction

sealed case class SetMask(mask: List[Option[Boolean]]) extends Instruction

sealed case class SetMemory(id: Long, value: Long) extends Instruction

object Instruction {
  val maskPattern: Regex = "^\\s*mask\\s*=\\s*([10X]+)\\s*$".r
  val memPattern: Regex = "^\\s*mem\\[(\\d+)]\\s*=\\s*(\\d+)\\s*$".r

  def parse(line: String): Instruction = {
    if (maskPattern.matches(line)) {
      val matches = maskPattern.findAllIn(line)
      SetMask(
        matches
          .group(1)
          .trim
          .toCharArray
          .map {
            case 'X' | 'x' => None
            case '1' => Some(true)
            case '0' => Some(false)
            case _ => throw new RuntimeException
          }
          .toList
      )
    } else if (memPattern.matches(line)) {
      val matches = memPattern.findAllIn(line)
      SetMemory(matches.group(1).trim.toLong, matches.group(2).trim.toLong)
    } else {
      throw new RuntimeException
    }
  }
}