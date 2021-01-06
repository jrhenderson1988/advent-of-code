package com.github.jrhenderson1988.adventofcode2020.day22

sealed trait Winner

sealed case class Player1(player: Player) extends Winner

sealed case class Player2(player: Player) extends Winner