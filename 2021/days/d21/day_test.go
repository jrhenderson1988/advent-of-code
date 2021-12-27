package d21

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestDeterministicDie_Play(t *testing.T) {
	die := NewDeterministicDie(100)
	game := NewGameFromPositions(4, 8)
	winningScore := die.Play(game)
	assert.Equal(t, 739785, winningScore)
}

func TestDiracDie_Play(t *testing.T) {
	die := NewDiracDie(3)
	game := NewGameFromPositions(4, 8)
	totalUniversesOfMostWinningPlayer := die.Play(game)
	assert.Equal(t, 444356092776315, totalUniversesOfMostWinningPlayer)
}
