package d12

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
	"unicode"
)

const start = "start"
const end = "end"

func Execute(input string) (days.Result, error) {
	cave, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalPossiblePaths := CalculateNumberOfPossiblePaths(cave)
	totalPossiblePaths2 := CalculateNumberOfPossiblePathsAllowingVisitingASmallCaveTwice(cave)

	return days.NewIntResult(totalPossiblePaths, totalPossiblePaths2), nil
}

func CalculateNumberOfPossiblePaths(caves map[string][]string) int {
	return totalPossiblePaths(caves, func(p path, next string) bool {
		if isSmallCave(next) && p.Contains(next) {
			return false
		}
		return true
	})
}

func CalculateNumberOfPossiblePathsAllowingVisitingASmallCaveTwice(caves map[string][]string) int {
	return totalPossiblePaths(caves, func(p path, next string) bool {
		if isSmallCave(next) && p.Contains(next) {
			if p.HasVisitedASmallCaveTwice() {
				return false
			}
		}
		return true
	})
}

func totalPossiblePaths(caves map[string][]string, expand func(p path, next string) bool) int {
	paths := make([]path, 0)
	paths = append(paths, []string{start})

	allComplete := false
	for !allComplete {
		allComplete = true

		newPaths := make([]path, 0)
		for _, p := range paths {
			if p.Complete() {
				newPaths = append(newPaths, p)
				continue
			}

			allComplete = false

			// totalPossiblePaths
			possibleNextSteps := caves[p.Last()]
			for _, possibleNextStep := range possibleNextSteps {
				if !expand(p, possibleNextStep) {
					continue
				}

				newPath := p.Clone()
				newPath = append(newPath, possibleNextStep)
				newPaths = append(newPaths, newPath)
			}
		}
		paths = newPaths
	}

	return len(paths)
}

func isSmallCave(cave string) bool {
	for _, ch := range []rune(cave) {
		if !unicode.IsLower(ch) {
			return false
		}
	}
	return true
}

func parseInput(input string) (map[string][]string, error) {
	paths := make(map[string][]string)
	for _, line := range common.SplitLines(strings.TrimSpace(input)) {
		parts := strings.Split(line, "-")
		if len(parts) != 2 {
			return nil, fmt.Errorf("invalid line: %s", line)
		}

		a := strings.TrimSpace(parts[0])
		b := strings.TrimSpace(parts[1])

		if b != start && a != end {
			targets, exists := paths[a]
			if !exists {
				targets = make([]string, 0)
			}
			targets = append(targets, b)
			paths[a] = targets
		}

		if a != start && b != end {
			inverseTargets, inverseExists := paths[b]
			if !inverseExists {
				inverseTargets = make([]string, 0)
			}
			inverseTargets = append(inverseTargets, a)
			paths[b] = inverseTargets
		}

	}
	return paths, nil
}
