package d15

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"math"
	"strings"
)

type coord struct {
	x int
	y int
}

func newCoord(x, y int) coord {
	return coord{x, y}
}

func Execute(input string) (days.Result, error) {
	cave, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	lowestRisk := CalculateLowestRisk(cave)
	lowestRiskTimes5 := CalculateLowestRiskAfterMultiplyingInput(cave, 5)

	return days.NewIntResult(lowestRisk, lowestRiskTimes5), nil
}

func parseInput(input string) ([][]int, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	cave := make([][]int, len(lines))
	lineLen := -1
	for y, line := range lines {
		cells := []rune(strings.TrimSpace(line))
		if lineLen == -1 {
			lineLen = len(cells)
		} else if len(cells) != lineLen {
			return nil, fmt.Errorf("uneven lines in input")
		}

		row := make([]int, lineLen)
		for x, cell := range cells {
			row[x] = int(cell) - 48
		}
		cave[y] = row
	}

	return cave, nil
}

func CalculateLowestRiskAfterMultiplyingInput(cave [][]int, multiplier int) int {
	cave = multiplyCave(cave, multiplier)
	return CalculateLowestRisk(cave)
}

func CalculateLowestRisk(cave [][]int) int {
	height := len(cave)
	if height == 0 {
		panic("invalid cave data, height is 0")
	}

	width := len(cave[0])
	if width == 0 {
		panic("invalid cave data, width is 0")
	}

	source := newCoord(0, 0)
	target := newCoord(width-1, height-1)

	dist := make(map[coord]int)
	prev := make(map[coord]coord)

	dist[source] = 0

	q := NewMinPriorityQueue()
	for y := 0; y < len(cave); y++ {
		for x := 0; x < len(cave[y]); x++ {
			v := newCoord(x, y)
			if x == 0 && y == 0 {
				q.Add(v, 0)
			} else {
				dist[v] = math.MaxInt32
			}
		}
	}

	for q.Len() > 0 {
		u := q.Pop()

		if u == target {
			s := traceShortestPath(source, target, prev)

			total := 0
			for _, pt := range s {
				if pt == source {
					continue
				}
				total += cave[pt.y][pt.x]
			}

			return total
		}

		for _, v := range neighboursOf(u) {
			if !withinBounds(v, width, height) {
				continue
			}

			alt := dist[u] + cave[v.y][v.x]
			if alt < dist[v] {
				dist[v] = alt
				prev[v] = u
				q.Add(v, alt)
			}
		}

	}

	return -1
}

func multiplyCave(cave [][]int, multiplier int) [][]int {
	if multiplier < 2 {
		panic("multiplier should be greater than 2")
	}

	origHeight := len(cave)
	origWidth := len(cave[0])
	newHeight := origHeight * multiplier
	newWidth := origWidth * multiplier

	newCave := make([][]int, len(cave)*multiplier)
	for y := 0; y < newHeight; y++ {
		yMod := y / origHeight
		row := make([]int, newWidth)
		for x := 0; x < newWidth; x++ {
			xMod := x / origWidth
			riskModifier := yMod + xMod
			value := cave[y%origHeight][x%origWidth] + riskModifier
			if value > 9 {
				value %= 9
			}
			row[x] = value
		}
		newCave[y] = row
	}

	return newCave
}

func neighboursOf(pt coord) []coord {
	return []coord{
		newCoord(pt.x-1, pt.y),
		newCoord(pt.x, pt.y-1),
		newCoord(pt.x+1, pt.y),
		newCoord(pt.x, pt.y+1),
	}
}

func withinBounds(pt coord, width, height int) bool {
	return pt.x >= 0 && pt.y >= 0 && pt.x < width && pt.y < height
}

func traceShortestPath(source, target coord, prev map[coord]coord) []coord {
	u := target
	s := make([]coord, 0)
	s = append(s, u)
	for u != source {
		s = append([]coord{prev[u]}, s...)
		u = prev[u]
	}
	return s
}
