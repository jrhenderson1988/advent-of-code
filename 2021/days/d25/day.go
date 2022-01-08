package d25

import (
	"aoc2021/common"
	"aoc2021/days"
	"strings"
)

func Execute(input string) (days.Result, error) {
	grid, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	step := FirstStepWithNoMoves(grid)

	return days.NewResult(common.IntToString(step), "N/A"), nil
}

func parseInput(input string) (map[point]cucumber, error) {
	grid := make(map[point]cucumber)
	for y, line := range common.SplitLines(strings.TrimSpace(input)) {
		for x, ch := range []rune(strings.TrimSpace(line)) {
			c, err := parseCucumber(ch)
			if err != nil {
				return nil, err
			}
			if c == none {
				continue
			}
			grid[newPoint(x, y)] = c
		}
	}

	return grid, nil
}

func FirstStepWithNoMoves(grid map[point]cucumber) int {
	max := getMaxPoint(grid)

	for i := 1; true; i++ {
		totalMoves := 0
		newGrid := make(map[point]cucumber)

		// east moving cucumbers
		for pt, c := range grid {
			if c != east {
				continue
			}

			target := newPoint(pt.x+1, pt.y)
			if target.x > max.x {
				target.x = 0
			}

			if _, exists := grid[target]; exists {
				newGrid[pt] = c
			} else {
				newGrid[target] = c
				totalMoves++
			}
		}

		// south moving cucumbers
		for pt, c := range grid {
			if c != south {
				continue
			}
			target := newPoint(pt.x, pt.y+1)
			if target.y > max.y {
				target.y = 0
			}

			if other, southExists := grid[target]; southExists && other == south {
				newGrid[pt] = c
			} else if _, eastExists := newGrid[target]; eastExists {
				newGrid[pt] = c
			} else {
				newGrid[target] = c
				totalMoves++
			}
		}

		if totalMoves == 0 {
			return i
		}

		grid = newGrid
	}

	return -1
}

func getMaxPoint(grid map[point]cucumber) point {
	maxY, maxX := 0, 0
	for pt := range grid {
		if pt.x > maxX {
			maxX = pt.x
		}
		if pt.y > maxY {
			maxY = pt.y
		}
	}
	return newPoint(maxX, maxY)
}
