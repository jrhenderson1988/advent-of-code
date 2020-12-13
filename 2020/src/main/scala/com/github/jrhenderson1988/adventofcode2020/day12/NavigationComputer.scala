package com.github.jrhenderson1988.adventofcode2020.day12

case class NavigationComputer(
                               instructions: List[Instruction],
                               shipPosition: (Int, Int),
                               shipDirection: Char,
                               waypointPosition: (Int, Int)
                             ) {
  private val directionOrder = List('N', 'E', 'S', 'W')

  def executeInstructionsAgainstShipAndReportDistance(): Int = {
    var cpu = this
    for (instruction <- instructions) {
      cpu = cpu.executeAgainstShip(instruction)
    }
    cpu.manhattanDistanceOfShip()
  }

  def executeInstructionsWithWaypointAndReportDistance(): Int = {
    var cpu = this
    for (instruction <- instructions) {
      cpu = cpu.executeAgainstWaypoint(instruction)
    }
    cpu.manhattanDistanceOfShip()
  }

  private def executeAgainstShip(instruction: Instruction): NavigationComputer = {
    instruction match {
      case Turn(direction, angle) =>turnShip(direction, angle)
      case Move(direction, value) => moveShip(direction, value)
      case Forward(value) => moveShip(shipDirection, value)
    }
  }

  private def executeAgainstWaypoint(instruction: Instruction): NavigationComputer = {
    instruction match {
      case Turn(direction, angle) => rotateWaypoint(direction, angle)
      case Move(direction, distance) => moveWaypoint(direction, distance)
      case Forward(distance) => moveShipTowardsWaypoint(distance)
    }
  }

  private def directionToDelta(value: Char): (Int, Int) = {
    value match {
      case 'N' => (0, -1)
      case 'E' => (1, 0)
      case 'S' => (0, 1)
      case 'W' => (-1, 0)
      case _ => throw new RuntimeException
    }
  }

  private def moveShip(direction: Char, distance: Int): NavigationComputer = {
    val delta = directionToDelta(direction)
    updateShip(
      (shipPosition._1 + (delta._1 * distance), shipPosition._2 + (delta._2 * distance)),
      shipDirection
    )
  }

  private def turnShip(direction: Char, angle: Int): NavigationComputer = {
    val hops = (angle / 90) % directionOrder.size
    val pos = directionOrder.indexOf(shipDirection)
    updateShip(shipPosition, directionOrder(
      direction match {
        case 'R' => (pos + hops) % directionOrder.size
        case 'L' => (pos + directionOrder.size - hops) % directionOrder.size
        case _ => throw new RuntimeException
      }
    ))
  }

  private def rotateWaypoint(direction: Char, angle: Int): NavigationComputer = {
    updateWaypoint((0 until (angle / 90)).foldLeft(waypointPosition) { (pos, _) =>
      if (direction == 'R') {
        (-pos._2, pos._1)
      } else {
        (pos._2, -pos._1)
      }
    })
  }

  private def moveWaypoint(direction: Char, distance: Int): NavigationComputer = {
    val delta = directionToDelta(direction)
    updateWaypoint((waypointPosition._1 + (delta._1 * distance), waypointPosition._2 + (delta._2 * distance)))
  }

  private def moveShipTowardsWaypoint(times: Int): NavigationComputer = {
    updateShip(
      (shipPosition._1 + (waypointPosition._1 * times), shipPosition._2 + (waypointPosition._2 * times)),
      shipDirection
    )
  }

  private def updateShip(position: (Int, Int), direction: Char): NavigationComputer = {
    NavigationComputer(instructions, position, direction, waypointPosition)
  }

  private def updateWaypoint(position: (Int, Int)): NavigationComputer = {
    NavigationComputer(instructions, shipPosition, shipDirection, position)
  }

  private def manhattanDistanceOfShip(): Int = {
    Math.abs(shipPosition._1) + Math.abs(shipPosition._2)
  }

  override def toString: String =
    s"NavigationComputer(Ship: $shipPosition facing $shipDirection, Waypoint: $waypointPosition)"
}

object NavigationComputer {
  def parse(lines: List[String]): NavigationComputer = {
    NavigationComputer(lines.map { line => Instruction.parse(line) }, (0, 0), 'E', (10, -1))
  }
}