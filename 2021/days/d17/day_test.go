package d17

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCalculateHighestTrajectoryToLandInTrench(t *testing.T) {
	target := newTargetArea(20, 30, -10, -5)
	highestY := CalculateHighestYPositionToStillLandInTarget(target)
	assert.Equal(t, 45, highestY)
}

func TestCalculateTotalDistinctInitialVelocities(t *testing.T) {
	target := newTargetArea(20, 30, -10, -5)
	totalInitialVelocities := CalculateTotalDistinctInitialVelocities(target)
	assert.Equal(t, 112, totalInitialVelocities)
}
