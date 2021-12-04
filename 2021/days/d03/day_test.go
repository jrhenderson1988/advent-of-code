package d03

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

var input = [][]bool{
	{false, false, true, false, false},
	{true, true, true, true, false},
	{true, false, true, true, false},
	{true, false, true, true, true},
	{true, false, true, false, true},
	{false, true, true, true, true},
	{false, false, true, true, true},
	{true, true, true, false, false},
	{true, false, false, false, false},
	{true, true, false, false, true},
	{false, false, false, true, false},
	{false, true, false, true, false},
}

//00100
//11110
//10110
//10111
//10101
//01111
//00111
//11100
//10000
//11001
//00010
//01010

//00100
//01111
//00111
//00010
//01010

//01111
//01010

//01010


func TestCalculatePowerConsumption(t *testing.T) {
	pc, err := CalculatePowerConsumption(input)

	assert.NoError(t, err)
	assert.Equal(t, 198, pc)
}

func TestCalculateLifeSupportRating(t *testing.T) {
	lsr, err := CalculateLifeSupportRating(input)

	assert.NoError(t, err)
	assert.Equal(t, 230, lsr)
}
