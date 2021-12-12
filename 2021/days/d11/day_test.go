package d11

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() [][]int {
	input := "5483143223\n" +
		"2745854711\n" +
		"5264556173\n" +
		"6141336146\n" +
		"6357385478\n" +
		"4167524645\n" +
		"2176841721\n" +
		"6882881134\n" +
		"4846848554\n" +
		"5283751526"

	octopuses, err := parseInput(input)
	if err != nil {
		panic(err)
	}

	return octopuses
}

func TestCalculateFlashesAfterSteps(t *testing.T) {
	input := getTestInput()
	flashes := CalculateFlashesAfterSteps(input, 100)

	assert.Equal(t, 1656, flashes)
}

func TestCalculateFirstSimultaneousFlashStep(t *testing.T) {
	input := getTestInput()
	flashes := CalculateFirstSimultaneousFlashStep(input)

	assert.Equal(t, 195, flashes)
}
