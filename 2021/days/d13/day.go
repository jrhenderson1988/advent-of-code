package d13

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

type foldInstruction struct {
	horizontal bool
	value      int
}

type coord struct {
	x int
	y int
}

func Execute(input string) (days.Result, error) {
	grid, instructions, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalAfterOneFold := TotalVisibleDotsAfterFirstFold(grid, instructions)
	code := FoldPaperToRevealCode(grid, instructions)

	return days.NewResult(common.IntToString(totalAfterOneFold), code), nil
}

func TotalVisibleDotsAfterFirstFold(grid map[coord]bool, instructions []foldInstruction) int {
	result := fold(grid, instructions[0])
	return len(result)
}

func FoldPaperToRevealCode(grid map[coord]bool, instructions []foldInstruction) string {
	result := grid
	for _, instruction := range instructions {
		result = fold(result, instruction)
	}

	maxX, maxY := 0, 0
	for pt, _ := range result {
		if pt.x > maxX {
			maxX = pt.x
		}
		if pt.y > maxY {
			maxY = pt.y
		}
	}

	sb := strings.Builder{}
	sb.WriteRune('\n')
	for y := 0; y <= maxY; y++ {
		for x := 0; x <= maxX; x++ {
			_, exists := result[coord{x, y}]
			if exists {
				sb.WriteRune('#')
			} else {
				sb.WriteRune('.')
			}
		}
		sb.WriteRune('\n')
	}

	return sb.String()
}

func fold(grid map[coord]bool, instruction foldInstruction) map[coord]bool {
	newGrid := make(map[coord]bool)
	for pt, _ := range grid {
		if instruction.horizontal {
			if pt.y < instruction.value {
				newGrid[pt] = true
			} else if pt.y > instruction.value {
				newY := instruction.value - (pt.y - instruction.value)
				newGrid[coord{x: pt.x, y: newY}] = true
			} else {
				panic("coordinate appears on the fold line")
			}
		} else {
			if pt.x < instruction.value {
				newGrid[pt] = true
			} else if pt.x > instruction.value {
				newX := instruction.value - (pt.x - instruction.value)
				newGrid[coord{x: newX, y: pt.y}] = true
			} else {
				panic("coordinate appears on the fold line")
			}
		}
	}

	return newGrid
}

func parseInput(input string) (map[coord]bool, []foldInstruction, error) {
	input = common.NormalizeLineBreaks(input)
	parts := strings.Split(input, "\n\n")
	if len(parts) != 2 {
		return nil, nil, fmt.Errorf("invalid input, expected 2 sections")
	}

	points := make(map[coord]bool)
	for _, line := range common.SplitLines(strings.TrimSpace(parts[0])) {
		coords := strings.Split(strings.TrimSpace(line), ",")
		if len(coords) != 2 {
			return nil, nil, fmt.Errorf("invalid input, expected an (x,y) coordinate")
		}

		x, err := common.StringToInt(strings.TrimSpace(coords[0]))
		if err != nil {
			return nil, nil, err
		}

		y, err := common.StringToInt(strings.TrimSpace(coords[1]))
		if err != nil {
			return nil, nil, err
		}

		pt := coord{x, y}
		points[pt] = true
	}

	instructions := make([]foldInstruction, 0)
	for _, line := range common.SplitLines(strings.TrimSpace(parts[1])) {
		line = strings.TrimSpace(line)
		if !strings.HasPrefix(line, "fold along ") {
			return nil, nil, fmt.Errorf("line does not have 'fold along ' prefix: %s", line)
		}

		instruction := strings.Split(line[11:], "=")
		if len(instruction) != 2 {
			return nil, nil, fmt.Errorf("line does not have a correct fold instruction")
		}

		axis := strings.TrimSpace(instruction[0])
		number := strings.TrimSpace(instruction[1])

		horizontal := false
		if axis == "y" {
			horizontal = true
		} else if axis == "x" {
			horizontal = false
		} else {
			return nil, nil, fmt.Errorf("invalid axis in fold instruction")
		}

		value, err := common.StringToInt(number)
		if err != nil {
			return nil, nil, err
		}

		instructions = append(instructions, foldInstruction{horizontal, value})
	}

	return points, instructions, nil
}
