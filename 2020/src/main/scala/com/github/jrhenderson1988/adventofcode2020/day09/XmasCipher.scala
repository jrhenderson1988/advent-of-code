package com.github.jrhenderson1988.adventofcode2020.day09

import scala.util.control.Breaks.{break, breakable}

case class XmasCipher(numbers: List[Long]) {
  def findFirstWeakness(preambleSize: Int, considerationSize: Int): Long = {
    (preambleSize until numbers.size)
      .map { index => (index, numbers(index)) }
      .find { case (index, number) =>
        val consideration = numbers.slice(index - considerationSize, index)
        !consideration.exists { num => consideration.diff(List(num)).contains(number - num) }
      }
      .get
      ._2
  }

  def findEncryptionWeakness(preambleSize: Int, considerationSize: Int): Long = {
    val firstWeakness = findFirstWeakness(preambleSize, considerationSize)
    for (index <- numbers.indices) {
      breakable {
        for (offset <- 1 until numbers.size - index) {
          val slice = numbers.slice(index, index + offset + 1)
          val sum = slice.sum
          if (sum == firstWeakness) {
            return slice.min + slice.max
          } else if (sum > firstWeakness) {
            break
          }
        }
      }
    }

    -1
  }
}

object XmasCipher {
  def parse(lines: List[String]): XmasCipher = {
    XmasCipher(lines.map(_.trim.toLong))
  }
}