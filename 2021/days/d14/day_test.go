package d14

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() ([]rune, map[pair]rune) {
	input := "NNCB\n" +
		"\n" +
		"CH -> B\n" +
		"HH -> N\n" +
		"CB -> H\n" +
		"NH -> C\n" +
		"HB -> C\n" +
		"HC -> B\n" +
		"HN -> C\n" +
		"NN -> C\n" +
		"BH -> H\n" +
		"NC -> B\n" +
		"NB -> B\n" +
		"BN -> B\n" +
		"BB -> N\n" +
		"BC -> B\n" +
		"CC -> N\n" +
		"CN -> C"

	template, rules, err := parseInput(input)
	if err != nil {
		panic(err)
	}
	return template, rules
}

func TestDiffBetweenMostAndLeastFrequentElementsAfterSteps(t *testing.T) {
	testCases := []struct {
		steps    int
		expected int
	}{
		{steps: 10, expected: 1588},
		{steps: 40, expected: 2188189693529},
	}

	for _, tc := range testCases {
		template, rules := getTestInput()
		diff := DiffAfterSteps(template, rules, tc.steps)
		assert.Equal(t, tc.expected, diff)
	}
}
