package com.github.jrhenderson1988.adventofcode2020.day22

sealed trait Winner

sealed case class Player1(player: Player) extends Winner

sealed case class Player2(player: Player) extends Winner

case class RecursiveCombat(player1: Player, player2: Player, previousRounds: Set[(Player, Player)]) {
  private def play(): Winner = {
    var game = this
    while (!game.finished()) {
      game = game.round()
    }

    game.winner()
  }

  private def round(): RecursiveCombat = {

    // If a previous round contains the same setup (both players have the same cards, in the same order) then the game
    // ends with player 1 as the winner. By removing all of player 2's cards, the next iteration of the loop in "play"
    // will result in game.finished() returning true and then game.winner() returning player 1 since that deck is not
    // empty.
    if (previousRounds.contains((player1, player2))) {
      return RecursiveCombat(player1, Player(List()), Set())
    }

    // If both players have a deck size which equals or exceeds that of their drawn cards (the drawn cards are not
    // counted in the deck size, hence the subtraction of 1) then another game is played to determine the winner.
    if ((player1.deck.size - 1) >= player1.deck.head && (player2.deck.size - 1) >= player2.deck.head) {
      val subGame = RecursiveCombat(
        Player(player1.deck.slice(1, player1.deck.head + 1)),
        Player(player2.deck.slice(1, player2.deck.head + 1)),
        Set()
      )
      subGame.play() match {
        case Player1(_) => RecursiveCombat(
          Player(player1.deck.drop(1) ++ List(player1.deck.head, player2.deck.head)),
          Player(player2.deck.drop(1)),
          previousRounds ++ Set((player1, player2))
        )
        case Player2(_) => RecursiveCombat(
          Player(player1.deck.drop(1)),
          Player(player2.deck.drop(1) ++ List(player2.deck.head, player1.deck.head)),
          previousRounds ++ Set((player1, player2))
        )
      }
    } else {
      // The player with the highest value card wins.
      if (player1.deck.head > player2.deck.head) {
        RecursiveCombat(
          Player(player1.deck.drop(1) ++ List(player1.deck.head, player2.deck.head)),
          Player(player2.deck.drop(1)),
          previousRounds ++ Set((player1, player2))
        )
      } else {
        RecursiveCombat(
          Player(player1.deck.drop(1)),
          Player(player2.deck.drop(1) ++ List(player2.deck.head, player1.deck.head)),
          previousRounds ++ Set((player1, player2))
        )
      }
    }
  }

  private def playSubGame(): Player = {
    throw new RuntimeException("playing another game")
  }

  private def finished(): Boolean = {
    player1.deck.isEmpty || player2.deck.isEmpty
  }

  private def winner(): Winner = {
    if (player1.deck.isEmpty) {
      Player2(player2)
    } else {
      Player1(player1)
    }
  }

  def winnersScore(): Int = {
    play() match {
      case Player1(player) => player.calculateScore()
      case Player2(player) => player.calculateScore()
    }
  }
}

object RecursiveCombat {
  def parse(input: String): RecursiveCombat = {
    val players = input.trim.split("\\n\\s*\\n").map(chunk => Player.parse(chunk.linesIterator.toList)).toList
    if (players.size != 2) {
      throw new RuntimeException
    }

    RecursiveCombat(players.head, players.last, Set())
  }
}
