package d02

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCalculateBasicDistanceFromOrigin(t *testing.T) {
	instructions := []instruction{
		{direction: Forward, amount: 5},
		{direction: Down, amount: 5},
		{direction: Forward, amount: 8},
		{direction: Up, amount: 3},
		{direction: Down, amount: 8},
		{direction: Forward, amount: 2},
	}
	assert.Equal(t, 150, CalculateBasicDistanceFromOrigin(instructions))
}

func TestCalculateMultiplicativeDistanceFromOrigin(t *testing.T) {
	instructions := []instruction{
		{direction: Forward, amount: 5},
		{direction: Down, amount: 5},
		{direction: Forward, amount: 8},
		{direction: Up, amount: 3},
		{direction: Down, amount: 8},
		{direction: Forward, amount: 2},
	}
	assert.Equal(t, 900, CalculateMultiplicativeDistanceFromOrigin(instructions))
}
