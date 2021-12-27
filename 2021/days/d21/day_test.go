package d21

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestGame_Play(t *testing.T) {
	game := NewGame(10, 4, 8, NewDeterministicDie(100))
	assert.Equal(t, 739785, game.Play())
}

