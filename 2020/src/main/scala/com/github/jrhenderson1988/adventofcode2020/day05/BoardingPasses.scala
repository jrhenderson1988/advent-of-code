package com.github.jrhenderson1988.adventofcode2020.day05

case class BoardingPasses(passes: List[BoardingPass]) {
  def highestSeatId(): Int = {
    passes.map(pass => pass.seatId()).max
  }

  def findMissingSeatId(): Int = {
    val seatIds = passes.map(pass => pass.seatId()).toSet
    for (id <- seatIds) {
      if (seatIds.contains(id + 2) && !seatIds.contains(id + 1)) {
        return id + 1
      } else if (seatIds.contains(id - 2) && !seatIds.contains(id - 1)) {
        return id - 1
      }
    }

    -1
  }
}

object BoardingPasses {
  def parse(lines: List[String]): BoardingPasses = {
    BoardingPasses(lines.map(line => BoardingPass(line.trim)))
  }
}
