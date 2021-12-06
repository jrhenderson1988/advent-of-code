package d06

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestTotalAfterDays(t *testing.T) {
	t.Run("total after 80 days", func(t *testing.T) {
		fish := []int{3, 4, 3, 1, 2}
		total := TotalAfterDays(fish, 80)

		assert.Equal(t, 5934, total)
	})

	t.Run("total after 256 days", func(t *testing.T) {
		fish := []int{3, 4, 3, 1, 2}
		total := TotalAfterDays(fish, 256)

		assert.Equal(t, 26984457539, total)
	})
}
