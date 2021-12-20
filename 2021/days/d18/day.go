package d18

import (
	"aoc2021/common"
	"aoc2021/days"
	"strings"
)

func Execute(input string) (days.Result, error) {
	numbers, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	magnitudeOfFinalSum := CalculateMagnitudeOfFinalSum(numbers)
	largestMagnitudeFromTwoNumbers := LargestMagnitudeFromTwoNumbers(numbers)

	return days.NewIntResult(magnitudeOfFinalSum, largestMagnitudeFromTwoNumbers), nil
}

func parseInput(input string) ([]*Number, error) {
	numbers := make([]*Number, 0)
	for _, line := range common.SplitLines(strings.TrimSpace(input)) {
		num, err := ParseNumber(strings.TrimSpace(line))
		if err != nil {
			return nil, err
		}
		numbers = append(numbers, num)
	}
	return numbers, nil
}

func CalculateMagnitudeOfFinalSum(numbers []*Number) int {
	sum := numbers[0]
	for _, num := range numbers[1:] {
		sum = sum.Add(num)
	}
	return sum.Magnitude()
}

func LargestMagnitudeFromTwoNumbers(numbers []*Number) int {
	largest := 0
	for i, a := range numbers {
		for j, b := range numbers {
			if i != j {
				sum := a.Add(b).Magnitude()
				if sum > largest {
					largest = sum
				}
			}
		}
	}

	return largest
}
