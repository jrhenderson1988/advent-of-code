package d21

type DeterministicDie struct {
	totalRolls int
	sides      int
}

func NewDeterministicDie(sides int) DeterministicDie {
	return DeterministicDie{sides: sides, totalRolls: 0}
}

func (dd DeterministicDie) Roll() (int, DeterministicDie) {
	value := (dd.totalRolls % dd.sides) + 1
	return value, DeterministicDie{totalRolls: dd.totalRolls + 1, sides: dd.sides}
}

func (dd DeterministicDie) Play(game Game) int {
	die := dd
	for {
		game, die = die.TakeTurn(game)
		if game.playerOne.Wins() {
			return game.playerTwo.score * die.totalRolls
		} else if game.playerTwo.Wins() {
			return game.playerOne.score * die.totalRolls
		}
	}
}

func (dd DeterministicDie) TakeTurn(game Game) (Game, DeterministicDie) {
	isPlayerOne := dd.totalRolls%6 == 0

	player := game.playerOne
	if !isPlayerOne {
		player = game.playerTwo
	}

	first, die := dd.Roll()
	second, die := die.Roll()
	third, die := die.Roll()
	newPosition := (player.position + first + second + third) % boardSize
	player = player.MoveToPosition(newPosition)

	var p1, p2 Player
	if !isPlayerOne {
		p1 = game.playerOne
		p2 = player
	} else {
		p1 = player
		p2 = game.playerTwo
	}

	return Game{
		playerOne: p1,
		playerTwo: p2,
	}, die
}
