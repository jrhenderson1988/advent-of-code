package d10

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"sort"
	"strings"
)

func Execute(input string) (days.Result, error) {
	subsystem, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	syntaxErrorScore := CalculateSyntaxErrorScore(subsystem)
	middleAutocompletionScore := CalculateMiddleAutoCompletionScore(subsystem)

	return days.NewIntResult(syntaxErrorScore, middleAutocompletionScore), nil
}

func CalculateSyntaxErrorScore(subsystem [][]rune) int {
	syntaxErrorScore := 0
	for _, line := range subsystem {
		illegalChar, isCorrupted := findFirstIllegalCharacter(line)
		if isCorrupted {
			if illegalChar == ')' {
				syntaxErrorScore += 3
			} else if illegalChar == ']' {
				syntaxErrorScore += 57
			} else if illegalChar == '}' {
				syntaxErrorScore += 1197
			} else if illegalChar == '>' {
				syntaxErrorScore += 25137
			} else {
				panic(fmt.Sprintf("unexpected illegal char found: %c", illegalChar))
			}
		}
	}
	return syntaxErrorScore
}

func CalculateMiddleAutoCompletionScore(subsystem [][]rune) int {
	scores := make([]int, 0)
	for _, line := range subsystem {
		_, isCorrupted := findFirstIllegalCharacter(line)
		if isCorrupted {
			continue
		}

		autocompleteSequence := findAutocompleteSequence(line)
		score := scoreAutocompleteSequence(autocompleteSequence)
		scores = append(scores, score)
	}

	sort.Slice(scores, func(a, b int) bool {
		return scores[a] < scores[b]
	})

	return scores[len(scores)/2]
}

func findFirstIllegalCharacter(line []rune) (rune, bool) {
	stack := make([]rune, 0)
	for _, ch := range line {
		if isOpeningChar(ch) {
			stack = append(stack, ch)
		} else if isClosingChar(ch) {
			matching := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			if ch != getClosingCharFor(matching) {
				return ch, true
			}
		}
	}
	return 0, false
}

func findAutocompleteSequence(line []rune) []rune {
	stack := make([]rune, 0)
	for _, ch := range line {
		if isOpeningChar(ch) {
			stack = append(stack, ch)
		} else if isClosingChar(ch) {
			matching := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			if ch != getClosingCharFor(matching) {
				panic("corrupted line")
			}
		}
	}

	sequence := make([]rune, len(stack))
	for i := 0; i < len(stack); i++ {
		sequence[i] = getClosingCharFor(stack[len(stack)-1-i])
	}

	return sequence
}

func scoreAutocompleteSequence(sequence []rune) int {
	score := 0
	for _, ch := range sequence {
		var points int
		if ch == ')' {
			points = 1
		} else if ch == ']' {
			points = 2
		} else if ch == '}' {
			points = 3
		} else if ch == '>' {
			points = 4
		} else {
			panic("unexpected closing char in autocomplete sequence")
		}

		score = (score * 5) + points
	}
	return score
}

func isOpeningChar(ch rune) bool {
	return ch == '(' || ch == '[' || ch == '{' || ch == '<'
}

func isClosingChar(ch rune) bool {
	return ch == ')' || ch == ']' || ch == '}' || ch == '>'
}

func getClosingCharFor(ch rune) rune {
	if ch == '(' {
		return ')'
	} else if ch == '[' {
		return ']'
	} else if ch == '{' {
		return '}'
	} else if ch == '<' {
		return '>'
	} else {
		panic(fmt.Sprintf("not an opening char: %c", ch))
	}
}

func parseInput(input string) ([][]rune, error) {
	subsystem := make([][]rune, 0)
	for _, inputLine := range common.SplitLines(strings.TrimSpace(input)) {
		line := make([]rune, 0)
		for _, ch := range []rune(strings.TrimSpace(inputLine)) {
			if isOpeningChar(ch) || isClosingChar(ch) {
				line = append(line, ch)
			} else {
				return nil, fmt.Errorf("invalid character detected: %c", ch)
			}
		}
		subsystem = append(subsystem, line)
	}
	return subsystem, nil
}
