package d21

const boardSize = 10

type Game struct {
	playerOne Player
	playerTwo Player
}

func NewGame(playerOnePos, playerTwoPos int) Game {
	if playerOnePos > boardSize || playerTwoPos > boardSize {
		panic("player initial position may not exceed board size")
	}

	return Game{
		playerOne: NewPlayer(playerOnePos-1, 0),
		playerTwo: NewPlayer(playerTwoPos-1, 0),
	}
}

