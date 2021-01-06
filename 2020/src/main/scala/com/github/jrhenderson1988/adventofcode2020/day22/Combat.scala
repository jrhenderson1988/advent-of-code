package com.github.jrhenderson1988.adventofcode2020.day22

trait Combat {
  val player1: Player
  val player2: Player

  def round(): Combat

  def winnersScore(): Int = {
    play() match {
      case Player1(player) => player.calculateScore()
      case Player2(player) => player.calculateScore()
    }
  }

  def play(): Winner = {
    var game = this
    while (!game.finished()) {
      game = game.round()
    }

    game.winner()
  }

  def finished(): Boolean = {
    player1.deck.isEmpty || player2.deck.isEmpty
  }

  def winner(): Winner = {
    if (player1.deck.isEmpty) {
      Player2(player2)
    } else if (player2.deck.isEmpty) {
      Player1(player1)
    } else {
      throw new RuntimeException("No winner")
    }
  }
}

object Combat {
  def parse[T <: Combat](input: String, builder: (Player, Player) => T): T = {
    val players = input.trim.split("\\n\\s*\\n").map(chunk => Player.parse(chunk.linesIterator.toList)).toList
    if (players.size != 2) {
      throw new RuntimeException
    }

    builder(players.head, players.last)
  }
}


