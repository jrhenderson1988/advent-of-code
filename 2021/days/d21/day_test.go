package d21

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestDeterministicDie_Play(t *testing.T) {
	die := NewDeterministicDie(100)
	game := NewGame(4, 8)
	winningScore := die.Play(game)
	assert.Equal(t, 739785, winningScore)
}
