package d05

import (
	"aoc2021/common"
	"aoc2021/days"
)

func Execute(input string) (days.Result, error) {
	lines, err := parseLines(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalOverlaps, err := CalculateTotalOverlappingPoints(lines, false)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalOverlapsWithDiagonals, err := CalculateTotalOverlappingPoints(lines, true)
	if err != nil {
		return days.EmptyResult(), err
	}

	return days.NewIntResult(totalOverlaps, totalOverlapsWithDiagonals), nil
}

func parseLines(input string) ([]Line, error) {
	lines := common.SplitLines(input)
	points := make([]Line, 0)
	for _, inputLine := range lines {
		line, err := ParseLine(inputLine)
		if err != nil {
			return nil, err
		}
		points = append(points, line)
	}
	return points, nil
}

func CalculateTotalOverlappingPoints(lines []Line, includeDiagonals bool) (int, error) {
	points := make(map[Point]int)
	for _, line := range lines {
		if includeDiagonals || (line.IsHorizontal() || line.IsVertical()) {
			for _, point := range line.PointsOnLine() {
				val, exists := points[point]
				if !exists {
					points[point] = 1
				} else {
					points[point] = val + 1
				}
			}
		}
	}

	total := 0
	for _, numLines := range points {
		if numLines > 1 {
			total++
		}
	}

	return total, nil
}
