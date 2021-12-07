package d07

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCalculateMinimumFuelCostToAlign(t *testing.T) {
	crabs := []int{16, 1, 2, 0, 4, 2, 7, 1, 2, 14}
	cost, err := CalculateMinimumFuelCostToAlign(crabs)

	assert.NoError(t, err)
	assert.Equal(t, 37, cost)
}

func TestCalculateMinimumRisingFuelCostToAlign(t *testing.T) {
	crabs := []int{16, 1, 2, 0, 4, 2, 7, 1, 2, 14}
	cost, err := CalculateMinimumRisingFuelCostToAlign(crabs)

	assert.NoError(t, err)
	assert.Equal(t, 168, cost)
}