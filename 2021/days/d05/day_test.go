package d05

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCalculateTotalOverlappingPoints(t *testing.T) {
	lines := []Line{
		NewLine(NewPoint(0, 9), NewPoint(5, 9)),
		NewLine(NewPoint(8, 0), NewPoint(0, 8)),
		NewLine(NewPoint(9, 4), NewPoint(3, 4)),
		NewLine(NewPoint(2, 2), NewPoint(2, 1)),
		NewLine(NewPoint(7, 0), NewPoint(7, 4)),
		NewLine(NewPoint(6, 4), NewPoint(2, 0)),
		NewLine(NewPoint(0, 9), NewPoint(2, 9)),
		NewLine(NewPoint(3, 4), NewPoint(1, 4)),
		NewLine(NewPoint(0, 0), NewPoint(8, 8)),
		NewLine(NewPoint(5, 5), NewPoint(8, 2)),
	}

	t.Run("with diagonals excluded", func(t *testing.T) {
		total, err := CalculateTotalOverlappingPoints(lines, false)

		assert.NoError(t, err)
		assert.Equal(t, 5, total)
	})

	t.Run("with diagonals included", func(t *testing.T) {
		total, err := CalculateTotalOverlappingPoints(lines, true)

		assert.NoError(t, err)
		assert.Equal(t, 12, total)
	})
}
