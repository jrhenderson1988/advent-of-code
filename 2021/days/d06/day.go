package d06

import (
	"aoc2021/common"
	"aoc2021/days"
	"strings"
)

func Execute(input string) (days.Result, error) {
	fish, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalAfter80Days := TotalAfterDays(fish, 80)
	totalAfter256Days := TotalAfterDays(fish, 256)

	return days.NewIntResult(totalAfter80Days, totalAfter256Days), nil
}

func TotalAfterDays(input []int, days int) int {
	fish := make(map[int]int)
	for _, daysRemaining := range input {
		if daysRemaining > 8 || daysRemaining < 0 {
			panic("Invalid input, only numbers between 0 and 8 are expected")
		}
		total, exists := fish[daysRemaining]
		if !exists {
			fish[daysRemaining] = 1
		} else {
			fish[daysRemaining] = total + 1
		}
	}

	for day := 0; day < days; day++ {
		newFish := make(map[int]int)
		for daysRemaining, total := range fish {
			newDaysRemaining := daysRemaining - 1
			if newDaysRemaining == -1 {
				newFish[8] = total
				newDaysRemaining = 6
			}

			currTotal, exists := newFish[newDaysRemaining]
			if exists {
				newFish[newDaysRemaining] = currTotal + total
			} else {
				newFish[newDaysRemaining] = total
			}
		}

		fish = newFish
	}

	totalFish := 0
	for _, total := range fish {
		totalFish += total
	}

	return totalFish
}

func parseInput(input string) ([]int, error) {
	return common.StringsToInts(strings.Split(strings.TrimSpace(input), ","))
}
