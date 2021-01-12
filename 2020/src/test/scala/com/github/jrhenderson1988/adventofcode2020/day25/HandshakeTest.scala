package com.github.jrhenderson1988.adventofcode2020.day25

import org.scalatest.FunSuite

class HandshakeTest extends FunSuite {
  test("loopCount") {
    assert(Handshake.loopSizeOf(5764801) == 8)
    assert(Handshake.loopSizeOf(17807724) == 11)
  }

  test("transform") {
    assert(Handshake.transform(17807724, 8) == 14897079)
  }

  test("encryptionKey") {
    assert(Handshake(5764801, 17807724).encryptionKey() == 14897079)
  }
}
