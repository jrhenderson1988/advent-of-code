package d09

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCalculateSumOfLowPointRiskLevels(t *testing.T) {
	input := [][]int{
		{2, 1, 9, 9, 9, 4, 3, 2, 1, 0},
		{3, 9, 8, 7, 8, 9, 4, 9, 2, 1},
		{9, 8, 5, 6, 7, 8, 9, 8, 9, 2},
		{8, 7, 6, 7, 8, 9, 6, 7, 8, 9},
		{9, 8, 9, 9, 9, 6, 5, 6, 7, 8},
	}

	sum := CalculateSumOfLowPointRiskLevels(input)
	assert.Equal(t, 15, sum)
}

func TestCalculateSizeOfLargestBasins(t *testing.T) {
	input := [][]int{
		{2, 1, 9, 9, 9, 4, 3, 2, 1, 0},
		{3, 9, 8, 7, 8, 9, 4, 9, 2, 1},
		{9, 8, 5, 6, 7, 8, 9, 8, 9, 2},
		{8, 7, 6, 7, 8, 9, 6, 7, 8, 9},
		{9, 8, 9, 9, 9, 6, 5, 6, 7, 8},
	}

	sum := CalculateSizeOfLargestBasins(input)
	assert.Equal(t, 1134, sum)
}
