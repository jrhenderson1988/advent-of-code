package d15

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() [][]int {
	input := "1163751742\n" +
		"1381373672\n" +
		"2136511328\n" +
		"3694931569\n" +
		"7463417111\n" +
		"1319128137\n" +
		"1359912421\n" +
		"3125421639\n" +
		"1293138521\n" +
		"2311944581"

	cave, err := parseInput(input)
	if err != nil {
		panic(err)
	}
	return cave
}

func TestCalculateLowestRisk(t *testing.T) {
	cave := getTestInput()
	risk := CalculateLowestRisk(cave)
	assert.Equal(t, 40, risk)
}

func TestCalculateLowestRiskAfterMultiplyingInput(t *testing.T) {
	cave := getTestInput()
	risk := CalculateLowestRiskAfterMultiplyingInput(cave, 5)
	assert.Equal(t, 315, risk)
}
