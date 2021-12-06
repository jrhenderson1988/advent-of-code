package d01

import (
	"aoc2021/common"
	"aoc2021/days"
)

func Execute(input string) (days.Result, error) {
	lines := common.SplitLines(input)

	totalIncreases, err := TotalIncreases(lines)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalIncreasesWithSlidingWindow, err := TotalIncreasesWithSlidingWindow(lines)
	if err != nil {
		return days.EmptyResult(), err
	}

	return days.NewIntResult(totalIncreases, totalIncreasesWithSlidingWindow), nil
}

func TotalIncreases(lines []string) (int, error) {
	values, err := common.StringsToInts(lines)
	if err != nil {
		return 0, err
	}
	previous := values[0]

	totalIncreases := 0
	for _, value := range values[1:] {
		if value > previous {
			totalIncreases++
		}
		previous = value
	}

	return totalIncreases, nil
}

func TotalIncreasesWithSlidingWindow(lines []string) (int, error) {
	values, err := common.StringsToInts(lines)
	if err != nil {
		return 0, err
	}
	previous := slidingWindowMeasurementAt(values, 0)

	totalIncreases := 0
	for i, _ := range values[:len(values)-2] {
		value := slidingWindowMeasurementAt(values, i)
		if value > previous {
			totalIncreases++
		}
		previous = value
	}

	return totalIncreases, nil
}

func slidingWindowMeasurementAt(values []int, index int) int {
	upper := index + 3
	if upper > len(values) {
		upper = len(values)
	}

	slice := values[index:upper]
	total := 0
	for _, value := range slice {
		total += value
	}

	return total
}
