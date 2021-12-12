package d11

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

func Execute(input string) (days.Result, error) {
	octopuses, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	flashesAfter100Steps := CalculateFlashesAfterSteps(octopuses, 100)
	firstSimultaneousFlashStep := CalculateFirstSimultaneousFlashStep(octopuses)

	return days.NewIntResult(flashesAfter100Steps, firstSimultaneousFlashStep), nil
}

func CalculateFlashesAfterSteps(input [][]int, steps int) int {
	octopuses := copyOctopuses(input)
	total := 0
	for i := 0; i < steps; i++ {
		octopuses = step(octopuses)
		for y := 0; y < len(octopuses); y++ {
			for x := 0; x < len(octopuses[y]); x++ {
				if octopuses[y][x] == 0 {
					total += 1
				}
			}
		}
	}

	return total
}

func CalculateFirstSimultaneousFlashStep(input [][]int) int {
	octopuses := copyOctopuses(input)

	currStep := 0
	for {
		octopuses = step(octopuses)
		allFlashed := true
		for y := 0; y < len(octopuses); y++ {
			for x := 0; x < len(octopuses[y]); x++ {
				if octopuses[y][x] != 0 {
					allFlashed = false
				}
			}
		}
		if allFlashed {
			return currStep + 1
		}

		currStep++
	}
}

func step(octopuses [][]int) [][]int {
	height := len(octopuses)
	width := len(octopuses[0])
	flashes := make(map[coord]bool)
	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			queue := make([]coord, 0)
			queue = append(queue, coord{x, y})
			for len(queue) > 0 {
				point := queue[0]
				queue = queue[1:]

				_, hasFlashed := flashes[point]
				if hasFlashed {
					continue
				}

				octopuses[point.y][point.x] += 1
				if octopuses[point.y][point.x] > 9 {
					flashes[point] = true
					for _, n := range neighbours(point.x, point.y) {
						if !withinBounds(n.x, n.y, width, height) {
							continue
						}

						queue = append(queue, n)
					}
				}
			}
		}
	}

	for pt := range flashes {
		octopuses[pt.y][pt.x] = 0
	}

	return octopuses
}

func neighbours(x, y int) []coord {
	return []coord{
		{x: x - 1, y: y - 1},
		{x: x, y: y - 1},
		{x: x + 1, y: y - 1},
		{x: x - 1, y: y},
		{x: x + 1, y: y},
		{x: x - 1, y: y + 1},
		{x: x, y: y + 1},
		{x: x + 1, y: y + 1},
	}
}

func withinBounds(x, y, width, height int) bool {
	return x >= 0 && x < width && y >= 0 && y < height
}

func parseInput(input string) ([][]int, error) {
	octopuses := make([][]int, 0)
	lineLength := -1
	for _, inputLine := range common.SplitLines(strings.TrimSpace(input)) {
		line := make([]int, 0)
		for _, ch := range []rune(strings.TrimSpace(inputLine)) {
			num := int(ch - 48)
			if num < 0 || num > 9 {
				return nil, fmt.Errorf("invalid octupus: %d", num)
			}
			line = append(line, num)
		}

		if lineLength == -1 {
			lineLength = len(line)
		} else if len(line) != lineLength {
			return nil, fmt.Errorf("uneven lines")
		}

		octopuses = append(octopuses, line)
	}
	return octopuses, nil
}

func copyOctopuses(octopuses [][]int) [][]int {
	cpy := make([][]int, len(octopuses))
	for y, line := range octopuses {
		lineCpy := make([]int, len(line))
		copy(lineCpy, line)
		cpy[y] = lineCpy
	}
	return cpy
}
