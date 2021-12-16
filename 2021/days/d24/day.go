package d24

import "aoc2021/days"

func Execute(input string) (days.Result, error) {
	value, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	p1 := Part1(value)
	p2 := Part2(value)

	return days.NewIntResult(p1, p2), nil
}

func parseInput(input string) (string, error) {
	return input, nil
}

func Part1(input string) int {
	return -1
}

func Part2(input string) int {
	return -1
}
