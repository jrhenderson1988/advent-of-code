package d21

type Game struct {
	boardSize int
	turns     int
	playerOne Player
	playerTwo Player
	die       DeterministicDie
}

func NewGame(boardSize, playerOnePos, playerTwoPos int, die DeterministicDie) Game {
	if playerOnePos > boardSize || playerTwoPos > boardSize {
		panic("player initial position may not exceed board size")
	}

	return Game{
		boardSize: boardSize,
		turns:     0,
		playerOne: NewPlayer(playerOnePos-1, 0),
		playerTwo: NewPlayer(playerTwoPos-1, 0),
		die:       die,
	}
}

func (g Game) TakeTurn() Game {
	player := g.playerOne
	if g.turns%2 == 1 {
		player = g.playerTwo
	}

	first, die := g.die.Roll()
	second, die := die.Roll()
	third, die := die.Roll()
	newPosition := (player.position + first + second + third) % g.boardSize
	player = player.MoveToPosition(newPosition)

	var playerOne, playerTwo Player
	if g.turns%2 == 1 {
		playerOne = g.playerOne
		playerTwo = player
	} else {
		playerOne = player
		playerTwo = g.playerTwo
	}

	return Game{
		boardSize: g.boardSize,
		turns:     g.turns + 1,
		playerOne: playerOne,
		playerTwo: playerTwo,
		die:       die,
	}
}

func (g Game) Play() int {
	game := g
	for {
		game = game.TakeTurn()
		if game.playerOne.Wins() {
			return game.playerTwo.score * game.die.totalRolls
		} else if game.playerTwo.Wins() {
			return game.playerOne.score * game.die.totalRolls
		}
	}
}

