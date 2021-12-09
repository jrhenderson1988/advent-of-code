package d08

import (
	"aoc2021/common"
	"aoc2021/days"
	"strings"
)

func Execute(input string) (days.Result, error) {
	diagnostics, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalIdentifiableDigits := CalculateTotalIdentifiableDigits(diagnostics)
	sumOfAllOutputs := CalculateSumOfAllOutputs(diagnostics)

	return days.NewIntResult(totalIdentifiableDigits, sumOfAllOutputs), nil
}

func CalculateTotalIdentifiableDigits(diagnostics []Diagnostic) int {
	total := 0
	for _, diagnostic := range diagnostics {
		for _, os := range diagnostic.OutputSignals() {
			l := os.Len()
			if l == 2 || l == 3 || l == 4 || l == 7 {
				total++
			}
		}
	}

	return total
}

func CalculateSumOfAllOutputs(diagnostics []Diagnostic) int {
	sum := 0
	for _, diagnostic := range diagnostics {
		sum += diagnostic.OutputAsNumber()
	}
	return sum
}

func parseInput(input string) ([]Diagnostic, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	signalsAndOutput := make([]Diagnostic, 0)
	for _, line := range lines {
		signalAndOutput, err := ParseDiagnostic(line)
		if err != nil {
			return nil, err
		}
		signalsAndOutput = append(signalsAndOutput, signalAndOutput)
	}
	return signalsAndOutput, nil
}
