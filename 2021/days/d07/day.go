package d07

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

func Execute(input string) (days.Result, error) {
	crabs, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	cost, err := CalculateMinimumFuelCostToAlign(crabs)
	if err != nil {
		return days.EmptyResult(), err
	}

	risingCost, err := CalculateMinimumRisingFuelCostToAlign(crabs)
	if err != nil {
		return days.EmptyResult(), err
	}

	return days.NewIntResult(cost, risingCost), nil
}

func CalculateMinimumFuelCostToAlign(crabs []int) (int, error) {
	return calculateFuelCostToAlign(crabs, func(crabPos int, targetPos int) int {
		return common.AbsInt(crabPos - targetPos)
	})
}

func CalculateMinimumRisingFuelCostToAlign(crabs []int) (int, error) {
	return calculateFuelCostToAlign(crabs, func(crabPos int, targetPos int) int {
		diff := common.AbsInt(crabPos - targetPos)
		// nth triangle number calculation
		return ((diff * diff) + diff) / 2
	})
}

func calculateFuelCostToAlign(crabs []int, calcFuelCost func (int, int) int) (int, error) {
	min, max := crabs[0], crabs[0]
	for _, crab := range crabs[1:] {
		if crab < min {
			min = crab
		}
		if crab > max {
			max = crab
		}
	}

	minFuelCost, first := 0, true
	for targetPosition := min; targetPosition <= max; targetPosition++ {
		cost := 0
		positions := cloneCrabs(crabs)
		for _, crabPosition := range positions {
			cost += calcFuelCost(crabPosition, targetPosition)
		}

		if first {
			minFuelCost = cost
			first = false
		} else if cost < minFuelCost {
			minFuelCost = cost
		}
	}

	if first {
		return 0, fmt.Errorf("no crabs")
	}

	return minFuelCost, nil
}

func parseInput(input string) ([]int, error) {
	return common.StringsToInts(strings.Split(strings.TrimSpace(input), ","))
}

func cloneCrabs(crabs []int) []int {
	clone := make([]int, len(crabs))
	copy(clone, crabs)
	return clone
}

