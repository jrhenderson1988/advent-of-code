package d12

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInputA() string {
	return "start-A\n" +
		"start-b\n" +
		"A-c\n" +
		"A-b\n" +
		"b-d\n" +
		"A-end\n" +
		"b-end"
}

func getTestInputB() string {
	return "dc-end\n" +
		"HN-start\n" +
		"start-kj\n" +
		"dc-start\n" +
		"dc-HN\n" +
		"LN-dc\n" +
		"HN-end\n" +
		"kj-sa\n" +
		"kj-HN\n" +
		"kj-dc"
}

func getTestInputC() string {
	return "fs-end\n" +
		"he-DX\n" +
		"fs-he\n" +
		"start-DX\n" +
		"pj-DX\n" +
		"end-zg\n" +
		"zg-sl\n" +
		"zg-pj\n" +
		"pj-he\n" +
		"RW-he\n" +
		"fs-DX\n" +
		"pj-RW\n" +
		"zg-RW\n" +
		"start-pj\n" +
		"he-WI\n" +
		"zg-he\n" +
		"pj-fs\n" +
		"start-RW"
}

func TestCalculateNumberOfPossiblePaths(t *testing.T) {
	testCases := []struct {
		input    string
		expected int
	}{
		{input: getTestInputA(), expected: 10},
		{input: getTestInputB(), expected: 19},
		{input: getTestInputC(), expected: 226},
	}

	for _, tc := range testCases {
		cave, err := parseInput(tc.input)
		if err != nil {
			panic(err)
		}

		total := CalculateNumberOfPossiblePaths(cave)
		assert.Equal(t, tc.expected, total)
	}
}

func TestCalculateNumberOfPossiblePathsAllowingVisitingASmallCaveTwice(t *testing.T) {
	testCases := []struct {
		input    string
		expected int
	}{
		{input: getTestInputA(), expected: 36},
		{input: getTestInputB(), expected: 103},
		{input: getTestInputC(), expected: 3509},
	}

	for _, tc := range testCases {
		cave, err := parseInput(tc.input)
		if err != nil {
			panic(err)
		}

		total := CalculateNumberOfPossiblePathsAllowingVisitingASmallCaveTwice(cave)
		assert.Equal(t, tc.expected, total)
	}
}
