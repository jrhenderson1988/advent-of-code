package d21

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestGame_Play(t *testing.T) {
	die := NewDeterministicDie(100)
	game := NewGame(10, 4, 8)
	winningScore := die.Play(game)
	assert.Equal(t, 739785, winningScore)
}

