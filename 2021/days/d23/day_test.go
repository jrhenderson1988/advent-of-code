package d23

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestMinimumEnergyToOrganiseAmphipods(t *testing.T) {
	testCases := []struct {
		input    Burrow
		expected int
	}{
		{
			input: NewBurrow(
				[]Amphipod{None, None, None, None, None, None, None, None, None, None, None},
				[]Amphipod{B, A},
				[]Amphipod{C, D},
				[]Amphipod{B, C},
				[]Amphipod{D, A},
			),
			expected: 12521,
		},
		{
			input: NewBurrow(
				[]Amphipod{None, None, None, None, None, None, None, None, None, None, None},
				[]Amphipod{B, D, D, A},
				[]Amphipod{C, C, B, D},
				[]Amphipod{B, B, A, C},
				[]Amphipod{D, A, C, A},
			),
			expected: 44169,
		},
	}

	for _, tc := range testCases {
		cost := MinimumEnergyToOrganiseAmphipods(tc.input)
		assert.Equal(t, tc.expected, cost)
	}
}
