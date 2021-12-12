package common_test

import (
	"aoc2021/common"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestIntToString(t *testing.T) {
	t.Run("valid integers", func(t *testing.T) {
		tests := []struct {
			input    int
			expected string
		}{
			{input: 123, expected: "123"},
			{input: 0, expected: "0"},
			{input: -9875123, expected: "-9875123"},
		}

		for _, test := range tests {
			result := common.IntToString(test.input)
			assert.Equal(t, test.expected, result)
		}
	})
}

func TestStringToInt(t *testing.T) {
	t.Run("input is not numeric", func(t *testing.T) {
		_, err := common.StringToInt("foo")
		assert.Error(t, err)
	})

	t.Run("input is numeric but not an integer", func(t *testing.T) {
		_, err := common.StringToInt("1.2")
		assert.Error(t, err)
	})

	t.Run("valid integers", func(t *testing.T) {
		tests := map[string]int{
			"0":       0,
			"123":     123,
			"-123456": -123456,
		}

		for input, expected := range tests {
			i, err := common.StringToInt(input)
			assert.NoError(t, err)
			assert.Equal(t, expected, i)
		}
	})
}

func TestStringsToInts(t *testing.T) {

	t.Run("slice with non-numeric strings", func(t *testing.T) {
		_, err := common.StringsToInts([]string{"foo", "bar"})
		assert.Error(t, err)
	})

	t.Run("slice with numeric but non-integer strings", func(t *testing.T) {
		_, err := common.StringsToInts([]string{"1.23", "-12356.65"})
		assert.Error(t, err)
	})

	t.Run("empty slice", func(t *testing.T) {
		ints, err := common.StringsToInts([]string{})
		assert.NoError(t, err)
		assert.Empty(t, ints)
	})

	t.Run("slice with valid integers", func(t *testing.T) {
		ints, err := common.StringsToInts([]string{"123", "0", "-567"})
		assert.NoError(t, err)
		assert.Equal(t, []int{123, 0, -567}, ints)
	})
}
