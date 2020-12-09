package com.github.jrhenderson1988.adventofcode2020.day09

import org.scalatest.FunSuite

class XmasCipherTest extends FunSuite {
  val input: String = "35\n20\n15\n25\n47\n40\n62\n55\n65\n95\n102\n117\n150\n182\n127\n219\n299\n277\n309\n576"
  val xmasCipher: XmasCipher = XmasCipher.parse(input.split("\\n").toList)

  test("parse") {
    assert(xmasCipher ==
      XmasCipher(List(35, 20, 15, 25, 47, 40, 62, 55, 65, 95, 102, 117, 150, 182, 127, 219, 299, 277, 309, 576)))
  }

  test("findFirstWeakness") {
    assert(xmasCipher.findFirstWeakness(5, 5) == 127)
  }

  test("findEncryptionWeakness") {
    assert(xmasCipher.findEncryptionWeakness(5, 5) == 62)
  }
}
