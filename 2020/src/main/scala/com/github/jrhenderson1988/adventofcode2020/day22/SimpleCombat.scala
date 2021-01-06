package com.github.jrhenderson1988.adventofcode2020.day22

case class SimpleCombat(player1: Player, player2: Player) extends Combat {
  override def round(): Combat = {
    if (player1.deck.head > player2.deck.head) {
      SimpleCombat(Player(player1.deck.drop(1) ++ List(player1.deck.head, player2.deck.head)), Player(player2.deck.drop(1)))
    } else {
      SimpleCombat(Player(player1.deck.drop(1)), Player(player2.deck.drop(1) ++ List(player2.deck.head, player1.deck.head)))
    }
  }
}

object SimpleCombat {
  def parse(input: String): SimpleCombat = {
    Combat.parse(input, (player1, player2) => SimpleCombat(player1, player2))
  }
}