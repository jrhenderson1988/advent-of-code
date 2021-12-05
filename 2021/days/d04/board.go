package d04

import (
	"fmt"
	"strings"
)

const boardSize = 5

type Board struct {
	grid  [boardSize][boardSize]int
	marks [boardSize][boardSize]bool
}

func NewBoard(input [][]int) (*Board, error) {
	var grid [boardSize][boardSize]int
	var marks [boardSize][boardSize]bool

	if len(input) != boardSize {
		return nil, fmt.Errorf("expected %d rows, %d provided", boardSize, len(input))
	}

	for y, row := range input {
		if len(row) != boardSize {
			return nil, fmt.Errorf("expected %d columns, row %d has %d", boardSize, y, len(row))
		}

		for x, square := range row {
			grid[y][x] = square
			marks[y][x] = false
		}
	}

	return &Board{grid, marks}, nil
}

func (b *Board) Mark(num int) {
	for y := 0; y < boardSize; y++ {
		for x := 0; x < boardSize; x++ {
			if b.grid[y][x] == num {
				b.marks[y][x] = true
			}
		}
	}
}

func (b *Board) Wins() bool {
	for i := 0; i < boardSize; i++ {
		winRow, winCol := true, true
		for j := 0; j < boardSize; j++ {
			if b.marks[i][j] == false {
				winRow = false
			}
			if b.marks[j][i] == false {
				winCol = false
			}
		}

		if winRow == true || winCol == true {
			return true
		}
	}

	return false
}

func (b *Board) Score() int {
	score := 0
	for y := 0; y < boardSize; y++ {
		for x := 0; x < boardSize; x++ {
			if !b.marks[y][x] {
				score += b.grid[y][x]
			}
		}
	}
	return score
}

func (b *Board) String() string {
	bldr := strings.Builder{}
	for y := 0; y < boardSize; y++ {
		for x := 0; x < boardSize; x++ {
			marked := " "
			if b.marks[y][x] {
				marked = "~"
			}
			bldr.WriteString(fmt.Sprintf("|%s%02d%s", marked, b.grid[y][x], marked))
		}
		bldr.WriteString("|\n")
	}
	return bldr.String()
}

func (b *Board) Clone() (*Board, error) {
	grid := make([][]int, len(b.grid))
	for y, row := range b.grid {
		rowCpy := make([]int, len(row))
		for x, cell := range row {
			rowCpy[x] = cell
		}
		grid[y] = rowCpy
	}

	clone, err := NewBoard(grid)
	if err != nil {
		return nil, err
	}

	return clone, nil
}
