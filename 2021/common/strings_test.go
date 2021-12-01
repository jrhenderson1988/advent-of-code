package common_test

import (
	"aoc2021/common"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestSplitLines(t *testing.T) {
	t.Run("empty string", func(t *testing.T) {
		result := common.SplitLines("")
		assert.Equal(t, []string{""}, result)
	})

	t.Run("string one line", func(t *testing.T) {
		result := common.SplitLines("foo bar")
		assert.Equal(t, []string{"foo bar"}, result)
	})

	t.Run("string with multiple lines", func(t *testing.T) {
		result := common.SplitLines("foo\nbar\nbaz")
		assert.Equal(t, []string{"foo", "bar", "baz"}, result)
	})

	t.Run("string with some empty lines", func(t *testing.T) {
		result := common.SplitLines("foo\n\n\nbar")
		assert.Equal(t, []string{"foo", "", "", "bar"}, result)
	})

	t.Run("string with windows style lines", func(t *testing.T) {
		result := common.SplitLines("foo\r\nbar\r\nbaz")
		assert.Equal(t, []string{"foo", "bar", "baz"}, result)
	})

	t.Run("string with multiple OS line styles", func(t *testing.T) {
		result := common.SplitLines("foo\r\nbar\rbaz\nqux")
		assert.Equal(t, []string{"foo", "bar", "baz", "qux"}, result)
	})
}
