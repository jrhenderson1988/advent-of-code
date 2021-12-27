package d21

type Game struct {
	boardSize int
	turns     int
	playerOne Player
	playerTwo Player
}

func NewGame(boardSize, playerOnePos, playerTwoPos int) Game {
	if playerOnePos > boardSize || playerTwoPos > boardSize {
		panic("player initial position may not exceed board size")
	}

	return Game{
		boardSize: boardSize,
		turns:     0,
		playerOne: NewPlayer(playerOnePos-1, 0),
		playerTwo: NewPlayer(playerTwoPos-1, 0),
	}
}

