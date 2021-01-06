package com.github.jrhenderson1988.adventofcode2020.day22

case class RecursiveCombat(player1: Player, player2: Player, rounds: Set[(Player, Player)]) extends Combat {
  def round(): RecursiveCombat = {
    if (rounds.contains((player1, player2))) {
      RecursiveCombat(player1, Player(List()), Set())
    } else if ((player1.deck.size - 1) >= player1.deck.head && (player2.deck.size - 1) >= player2.deck.head) {
      createSubGame().play() match {
        case Player1(_) => player1WinsRound()
        case Player2(_) => player2WinsRound()
      }
    } else if (player1.deck.head > player2.deck.head) {
      player1WinsRound()
    } else {
      player2WinsRound()
    }
  }

  def player1WinsRound(): RecursiveCombat = {
    RecursiveCombat(
      Player(player1.deck.drop(1) ++ List(player1.deck.head, player2.deck.head)),
      Player(player2.deck.drop(1)),
      rounds ++ Set((player1, player2))
    )
  }

  def player2WinsRound(): RecursiveCombat = {
    RecursiveCombat(
      Player(player1.deck.drop(1)),
      Player(player2.deck.drop(1) ++ List(player2.deck.head, player1.deck.head)),
      rounds ++ Set((player1, player2))
    )
  }

  def createSubGame(): RecursiveCombat = {
    RecursiveCombat(
      Player(player1.deck.slice(1, player1.deck.head + 1)),
      Player(player2.deck.slice(1, player2.deck.head + 1)),
      Set()
    )
  }
}

object RecursiveCombat {
  def parse(input: String): RecursiveCombat = {
    Combat.parse(input, (player1, player2) => RecursiveCombat(player1, player2, Set()))
  }
}
