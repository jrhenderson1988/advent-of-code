package common

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestAbsInt(t *testing.T) {
	tests := []struct {
		input    int
		expected int
	}{
		{input: 123, expected: 123},
		{input: 0, expected: 0},
		{input: -9875123, expected: 9875123},
	}

	for _, tc := range tests {
		assert.Equal(t, tc.expected, AbsInt(tc.input))
	}
}

func TestMaxInt(t *testing.T) {
	tests := []struct {
		a        int
		b        int
		expected int
	}{
		{a: 1, b: 2, expected: 2},
		{a: 1, b: 0, expected: 1},
		{a: -1, b: -2, expected: -1},
		{a: -1, b: 0, expected: 0},
		{a: 100, b: 101, expected: 101},
		{a: -999, b: 998, expected: 998},
	}

	for _, tc := range tests {
		assert.Equal(t, tc.expected, MaxInt(tc.a, tc.b))
	}
}

func TestMinInt(t *testing.T) {
	tests := []struct {
		a        int
		b        int
		expected int
	}{
		{a: 1, b: 2, expected: 1},
		{a: 1, b: 0, expected: 0},
		{a: -1, b: -2, expected: -2},
		{a: -1, b: 0, expected: -1},
		{a: 100, b: 101, expected: 100},
		{a: -999, b: 998, expected: -999},
	}

	for _, tc := range tests {
		assert.Equal(t, tc.expected, MinInt(tc.a, tc.b))
	}
}
