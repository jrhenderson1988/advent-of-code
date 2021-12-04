package common

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestBinarySliceToUint64(t *testing.T) {
	testCases := []struct {
		input    []bool
		expected uint64
	}{
		{input: []bool{true, false, false, false, false, false, false, false}, expected: 128},
		{input: []bool{true, true, true, true, true, true, true, true}, expected: 255},
		{input: []bool{false, false, false, false}, expected: 0},
		{input: []bool{false, true}, expected: 1},
		{input: []bool{}, expected: 0},
		{input: []bool{true, false, false, false, false, false, false, false, false}, expected: 256},
	}

	for _, tc := range testCases {
		assert.Equal(t, tc.expected, BinarySliceToUint64(tc.input))
	}
}
