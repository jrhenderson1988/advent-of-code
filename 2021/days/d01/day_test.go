package d01

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestTotalIncreases(t *testing.T) {
	input := []string{"199", "200", "208", "210", "200", "207", "240", "269", "260", "263"}
	result, err := TotalIncreases(input)
	assert.NoError(t, err)
	assert.Equal(t, 7, result)
}

func TestTotalIncreasesWithSlidingWindow(t *testing.T) {
	input := []string{"199", "200", "208", "210", "200", "207", "240", "269", "260", "263"}
	result, err := TotalIncreasesWithSlidingWindow(input)
	assert.NoError(t, err)
	assert.Equal(t, 5, result)
}
