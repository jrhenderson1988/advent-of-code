package com.github.jrhenderson1988.adventofcode2020.day25

import com.github.jrhenderson1988.adventofcode2020.day25.Handshake.transform

case class Handshake(cardPublicKey: Long, doorPublicKey: Long) {
  def encryptionKey(): Long = transform(cardPublicKey, Handshake.loopSizeOf(doorPublicKey))
}

object Handshake {
  val MOD_NUMBER = 20201227

  def parse(lines: List[String]): Handshake = {
    val keys = lines.map(_.trim.toLong)
    Handshake(keys.head, keys.last)
  }

  def loopSizeOf(key: Long): Long = {
    var value = 1
    val subjectNumber = 7
    var lc = 0
    while (true) {
      if (value == key) {
        return lc
      }
      lc += 1
      value = (value * subjectNumber) % MOD_NUMBER
    }
    -1
  }

  def transform(subjectNumber: Long, loopSize: Long): Long =
    (0 until loopSize.toInt)
      .foldLeft(1L) { (value, _) => (value * subjectNumber.toLong) % MOD_NUMBER }
}