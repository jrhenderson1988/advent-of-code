package d04

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func createTestBoard() *Board {
	board, _ := NewBoard([][]int{
		{22, 13, 17, 11, 0},
		{8, 2, 23, 4, 24},
		{21, 9, 14, 16, 7},
		{6, 10, 3, 18, 5},
		{1, 12, 20, 15, 19},
	})
	return board
}

func TestWins(t *testing.T) {
	t.Run("top row all marked", func(t *testing.T) {
		board := createTestBoard()
		board.Mark(22)
		board.Mark(13)
		board.Mark(17)
		board.Mark(11)
		board.Mark(0)
		assert.True(t, board.Wins())
	})

	t.Run("last row row all marked", func(t *testing.T) {
		board := createTestBoard()
		board.Mark(1)
		board.Mark(12)
		board.Mark(20)
		board.Mark(15)
		board.Mark(19)
		assert.True(t, board.Wins())
	})

	t.Run("middle column all marked", func(t *testing.T) {
		board := createTestBoard()
		board.Mark(17)
		board.Mark(23)
		board.Mark(14)
		board.Mark(3)
		board.Mark(20)
		assert.True(t, board.Wins())
	})

	t.Run("last column all marked", func(t *testing.T) {
		board := createTestBoard()
		board.Mark(0)
		board.Mark(24)
		board.Mark(7)
		board.Mark(5)
		board.Mark(19)
		assert.True(t, board.Wins())
	})

	t.Run("empty board", func(t *testing.T) {
		board := createTestBoard()
		assert.False(t, board.Wins())
	})

	t.Run("no winning lines", func(t *testing.T) {
		board := createTestBoard()
		board.Mark(8)
		board.Mark(9)
		board.Mark(3)
		board.Mark(15)
		assert.False(t, board.Wins())
	})
}

func TestScore(t *testing.T) {
	board, _ := NewBoard([][]int{
		{14, 21, 17, 24, 4},
		{10, 16, 15, 9, 19},
		{18, 8, 23, 26, 20},
		{22, 11, 13, 6, 5},
		{2, 0, 12, 3, 7},
	})

	board.Mark(7)
	board.Mark(4)
	board.Mark(9)
	board.Mark(5)
	board.Mark(11)
	board.Mark(17)
	board.Mark(23)
	board.Mark(2)
	board.Mark(0)
	board.Mark(14)
	board.Mark(21)
	board.Mark(24)

	assert.Equal(t, 188, board.Score())
}
