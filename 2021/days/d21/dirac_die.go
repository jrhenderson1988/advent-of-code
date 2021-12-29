package d21

const diracWinningScore = 21

type scores struct {
	playerOne int
	playerTwo int
}

func newScores(playerOne, playerTwo int) scores {
	return scores{playerOne, playerTwo}
}

type DiracDie struct {
	sides int
}

func NewDiracDie(sides int) DiracDie {
	return DiracDie{sides}
}

// Note: General idea for Part 2 was taken from jonathanpaulson's implementation in Python:
// https://github.com/jonathanpaulson/AdventOfCode/blob/master/2021/21.py
func (dd DiracDie) countWins(state Game, cache map[Game]scores) scores {
	// player one's score >= winning score -> return 1 way to win for player 1 and 0 for player 2
	if state.playerOne.score >= diracWinningScore {
		return newScores(1, 0)
	}

	// player two's score >= winning score -> return 1 way to win for player 2 and 0 for player 1
	if state.playerTwo.score >= diracWinningScore {
		return newScores(0, 1)
	}

	// if we've previously memoized the result of this game state, return it
	cached, existsInCache := cache[state]
	if existsInCache {
		return cached
	}

	universes := newScores(0, 0)

	// for each of the possible rolls of each die (3x3x3 = 27 possibilities)
	for d1 := 1; d1 <= 3; d1++ {
		for d2 := 1; d2 <= 3; d2++ {
			for d3 := 1; d3 <= 3; d3++ {
				// work out the new position and new score of player one given the combination
				newPosition := (state.playerOne.position + d1 + d2 + d3) % boardSize
				newScore := state.playerOne.score + newPosition + 1

				// create a new game state, swapping the players around so that player two is
				// considered next, and recurse into the next level.
				wins := dd.countWins(NewGame(state.playerTwo, NewPlayer(newPosition, newScore)), cache)

				// when we get back the wins, we add to the total universe count for each player;
				// since we swapped the order of the players when calling into the next level, we
				// need to add player one's wins to player two's universe and vice versa.
				universes = newScores(universes.playerOne+wins.playerTwo, universes.playerTwo+wins.playerOne)
			}
		}
	}

	// memoize the result of the calculating all possibilities for this game state.
	cache[state] = universes
	return universes
}

func (dd DiracDie) Play(game Game) int {
	wins := dd.countWins(game, make(map[Game]scores))
	if wins.playerOne > wins.playerTwo {
		return wins.playerOne
	} else {
		return wins.playerTwo
	}
}
