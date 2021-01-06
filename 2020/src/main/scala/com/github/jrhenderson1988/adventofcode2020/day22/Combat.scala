package com.github.jrhenderson1988.adventofcode2020.day22

case class Combat(player1: Player, player2: Player) {
  private def round(): Combat = {
    if (player1.deck.head > player2.deck.head) {
      Combat(Player(player1.deck.drop(1) ++ List(player1.deck.head, player2.deck.head)), Player(player2.deck.drop(1)))
    } else {
      Combat(Player(player1.deck.drop(1)), Player(player2.deck.drop(1) ++ List(player2.deck.head, player1.deck.head)))
    }
  }

  private def finished(): Boolean = {
    player1.deck.isEmpty || player2.deck.isEmpty
  }

  private def play(): Player = {
    var game = this
    while (!game.finished()) {
      game = game.round()
    }

    if (game.player1.deck.nonEmpty) {
      game.player1
    } else {
      game.player2
    }
  }

  def winnersScore(): Int = {
    play().calculateScore()
  }
}

object Combat {
  def parse(input: String): Combat = {
    val players = input.trim.split("\\n\\s*\\n").map(chunk => Player.parse(chunk.linesIterator.toList)).toList
    if (players.size != 2) {
      throw new RuntimeException
    }

    Combat(players.head, players.last)
  }
}