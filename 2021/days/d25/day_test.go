package d25

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() map[point]cucumber {
	input := "v...>>.vv>\n" +
			".vv>>.vv..\n" +
			">>.>v>...v\n" +
			">>v>>.>.v.\n" +
			"v>v.vv.v..\n" +
			">.>>..v...\n" +
			".vv..>.>v.\n" +
			"v.v..>>v.v\n" +
			"....v..v.>"
	grid, err := parseInput(input)
	if err != nil {
		panic(err)
	}

	return grid
}

func TestFirstStepWithNoMoves(t *testing.T) {
	grid := getTestInput()
	step := FirstStepWithNoMoves(grid)
	assert.Equal(t, 58, step)
}
