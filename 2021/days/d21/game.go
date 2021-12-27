package d21

const boardSize = 10

type Game struct {
	playerOne Player
	playerTwo Player
}

func NewGameFromPositions(playerOnePos, playerTwoPos int) Game {
	return Game{
		playerOne: NewPlayer(playerOnePos-1, 0),
		playerTwo: NewPlayer(playerTwoPos-1, 0),
	}
}

func NewGame(playerOne, playerTwo Player) Game {
	if playerOne.position >= boardSize || playerTwo.position >= boardSize {
		panic("player initial position may not exceed board size")
	} else if playerOne.position < 0 || playerTwo.position < 0 {
		panic("player initial position may not be less than 0")
	}
	return Game{playerOne, playerTwo}
}

