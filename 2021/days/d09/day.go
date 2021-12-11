package d09

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

type coord struct {
	x int
	y int
}

func (c coord) String() string {
	return fmt.Sprintf("(%d, %d)", c.x, c.y)
}

func Execute(input string) (days.Result, error) {
	heightmap, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	sumOfLowPointRiskLevels := CalculateSumOfLowPointRiskLevels(heightmap)
	sizeOfLargestBasins := CalculateSizeOfLargestBasins(heightmap)

	return days.NewIntResult(sumOfLowPointRiskLevels, sizeOfLargestBasins), nil
}

func CalculateSumOfLowPointRiskLevels(heightmap [][]int) int {
	sum := 0
	lowPoints := findLowPoints(heightmap)
	for _, lp := range lowPoints {
		sum += heightmap[lp.y][lp.x] + 1
	}
	return sum
}

func CalculateSizeOfLargestBasins(heightmap [][]int) int {
	height, width := len(heightmap), len(heightmap[0])

	// find low points
	lowPoints := findLowPoints(heightmap)

	// calculate size of craters
	basins := make(map[coord]int)
	for _, lp := range lowPoints {
		crater := make(map[coord]bool)
		visited := make(map[coord]bool)
		queue := make([]coord, 0)
		queue = append(queue, coord{x: lp.x, y: lp.y})
		for len(queue) > 0 {
			point := queue[0]
			queue = queue[1:]

			if point.y < 0 || point.y >= height || point.x < 0 || point.x >= width {
				continue
			} else if heightmap[point.y][point.x] == 9 {
				continue
			}

			crater[point] = true

			for _, n := range neighboursOf(point) {
				_, exists := crater[n]
				_, pointAlreadySeen := visited[n]
				if !exists && !pointAlreadySeen {
					visited[n] = true
					queue = append(queue, n)
				}
			}
		}

		basins[lp] = len(crater)
	}

	largest := make([]int, 3)
	for _, value := range basins {
		if value > largest[0] {
			largest[2] = largest[1]
			largest[1] = largest[0]
			largest[0] = value
		} else if value > largest[1] {
			largest[2] = largest[1]
			largest[1] = value
		} else if value > largest[2] {
			largest[2] = value
		}
	}

	return largest[0] * largest[1] * largest[2]
}

func findLowPoints(heightmap [][]int) []coord {
	lowPoints := make([]coord, 0)
	for y := 0; y < len(heightmap); y++ {
		for x := 0; x < len(heightmap[y]); x++ {
			isLowPoint := true

			for _, n := range neighboursOf(coord{x, y}) {
				if !withinBounds(n, len(heightmap[y]), len(heightmap)) {
					continue
				}

				if heightmap[n.y][n.x] <= heightmap[y][x] {
					isLowPoint = false
				}
			}

			if isLowPoint {
				lowPoints = append(lowPoints, coord{x, y})
			}
		}
	}
	return lowPoints
}

func neighboursOf(point coord) []coord {
	return []coord{
		{x: point.x - 1, y: point.y},
		{x: point.x + 1, y: point.y},
		{x: point.x, y: point.y - 1},
		{x: point.x, y: point.y + 1},
	}
}

func withinBounds(point coord, width, height int) bool {
	return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height
}

func parseInput(input string) ([][]int, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	result := make([][]int, len(lines))
	rowLen := -1
	for y, line := range lines {
		trimmedLine := []rune(strings.TrimSpace(line))
		if rowLen == -1 {
			rowLen = len(trimmedLine)
		} else if len(trimmedLine) != rowLen {
			return nil, fmt.Errorf("uneven line lengths")
		}

		row := make([]int, rowLen)
		for x, ch := range trimmedLine {
			if ch < '0' || ch > '9' {
				return nil, fmt.Errorf("invalid input, received non-numeric character %c", ch)
			}
			// -48 to go from ASCII char representation of digit to actual integer
			row[x] = int(ch) - 48
		}
		result[y] = row
	}
	return result, nil
}
