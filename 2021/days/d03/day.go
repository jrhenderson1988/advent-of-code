package d03

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

func Execute(input string) (days.Result, error) {
	readings, err := parseDiagnosticReport(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	powerConsumption, err := CalculatePowerConsumption(readings)
	if err != nil {
		return days.EmptyResult(), err
	}

	lifeSupportRating, err := CalculateLifeSupportRating(readings)
	if err != nil {
		return days.EmptyResult(), err
	}

	result := days.NewIntResult(powerConsumption, lifeSupportRating)
	return result, nil
}

func CalculatePowerConsumption(readings [][]bool) (int, error) {
	gamma, err := calculateGammaRate(readings)
	if err != nil {
		return 0, err
	}

	epsilon, err := calculateEpsilonRate(readings)
	if err != nil {
		return 0, err
	}

	return gamma * epsilon, nil
}

func CalculateLifeSupportRating(readings [][]bool) (int, error) {
	oxygenGeneratorRating, err := calculateOxygenGeneratorReading(readings)
	if err != nil {
		return 0, err
	}

	cO2ScrubberRating, err := calculateC02ScrubberReading(readings)
	if err != nil {
		return 0, err
	}

	return oxygenGeneratorRating * cO2ScrubberRating, nil
}

func parseDiagnosticReport(report string) ([][]bool, error) {
	lines := common.SplitLines(strings.TrimSpace(report))
	readings := make([][]bool, len(lines))
	for i, line := range lines {
		line := strings.TrimSpace(line)
		reading := make([]bool, len(line))
		for j, c := range line {
			if c == '1' {
				reading[j] = true
			} else if c == '0' {
				reading[j] = false
			} else {
				return nil, fmt.Errorf("unexpected character")
			}
		}
		readings[i] = reading
	}
	return readings, nil
}

func calculateGammaRate(readings [][]bool) (int, error) {
	return mergeReadings(readings, func(ones, zeroes int) bool {
		if ones > zeroes {
			return true
		}
		return false
	})
}

func calculateEpsilonRate(readings [][]bool) (int, error) {
	return mergeReadings(readings, func(ones, zeroes int) bool {
		if ones > zeroes {
			return false
		}
		return true
	})
}

func calculateOxygenGeneratorReading(readings [][]bool) (int, error) {
	return filterReadings(readings, func(ones, zeroes int) bool {
		if ones >= zeroes {
			return true
		} else {
			return false
		}
	})
}

func calculateC02ScrubberReading(readings [][]bool) (int, error) {
	return filterReadings(readings, func(ones, zeroes int) bool {
		if ones < zeroes {
			return true
		}
		return false
	})
}

func mergeReadings(readings [][]bool, determineBit func(ones, zeroes int) bool) (int, error) {
	oneCounts, err := countOneBits(readings)
	if err != nil {
		return 0, err
	}

	resultingBits := make([]bool, len(readings[0]))
	for i, totalOnes := range oneCounts {
		totalZeroes := len(readings) - totalOnes
		if totalZeroes == totalOnes {
			return 0, fmt.Errorf("same number of ones and zeroes")
		}

		resultingBits[i] = determineBit(totalOnes, totalZeroes)
	}

	return int(common.BinarySliceToUint64(resultingBits)), nil
}

func filterReadings(readings [][]bool, determineKeep func(ones, zeroes int) bool) (int, error) {
	filtered := copyReadings(readings)
	for len(filtered) > 1 {
		ones, err := countOneBits(filtered)
		if err != nil {
			return 0, err
		}

		zeroes := make([]int, len(ones))
		for i, numOnes := range ones {
			zeroes[i] = len(filtered) - numOnes
		}

		var bitIdx int
		var keep bool
		for i := range ones {
			if ones[i] == 0 || zeroes[i] == 0 {
				continue
			}

			bitIdx = i
			keep = determineKeep(ones[i], zeroes[i])
			break
		}

		for i := 0; i < len(filtered); i++ {
			num := filtered[i]
			if num[bitIdx] != keep {
				// remove an item by swapping it with the last item and updating the slice to
				// exclude the last item from its bounds. We need to make sure to decrement i, since
				// it will contain a different element following the swap and needs to be
				// re-evaluated.
				filtered[i] = filtered[len(filtered)-1]
				filtered = filtered[:len(filtered)-1]
				i -= 1

				if len(filtered) == 1 {
					break
				}
			}
		}
	}

	return int(common.BinarySliceToUint64(filtered[0])), nil
}

func countOneBits(readings [][]bool) ([]int, error) {
	if len(readings) < 1 {
		return nil, fmt.Errorf("no readings provided")
	}

	numBits := len(readings[0])

	oneCounts := make([]int, numBits)
	for _, reading := range readings {
		if len(reading) != numBits {
			return nil, fmt.Errorf("uneven number of bits in readings")
		}

		for i, b := range reading {
			if b {
				oneCounts[i]++
			}
		}
	}

	return oneCounts, nil
}

func copyReadings(readings [][]bool) [][]bool {
	cpy := make([][]bool, len(readings))
	for i, reading := range readings {
		readingCpy := make([]bool, len(reading))
		_ = copy(readingCpy, reading)
		cpy[i] = readingCpy
	}

	return cpy
}
