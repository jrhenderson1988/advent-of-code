package d10

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() [][]rune {
	input := "[({(<(())[]>[[{[]{<()<>>\n" +
		"[(()[<>])]({[<{<<[]>>(\n" +
		"{([(<{}[<>[]}>{[]{[(<()>\n" +
		"(((({<>}<{<{<>}{[]{[]{}\n" +
		"[[<[([]))<([[{}[[()]]]\n" +
		"[{[{({}]{}}([{[{{{}}([]\n" +
		"{<[[]]>}<{[{[{[]{()[[[]\n" +
		"[<(<(<(<{}))><([]([]()\n" +
		"<{([([[(<>()){}]>(<<{{\n" +
		"<{([{{}}[<[[[<>{}]]]>[]]"

	subsystem, err := parseInput(input)
	if err != nil {
		panic(err)
	}
	return subsystem
}

func TestCalculateSyntaxErrorScore(t *testing.T) {
	subsystem := getTestInput()
	score := CalculateSyntaxErrorScore(subsystem)

	assert.Equal(t, 26397, score)
}

func TestCalculateMiddleAutoCompletionScore(t *testing.T) {
	subsystem := getTestInput()
	score := CalculateMiddleAutoCompletionScore(subsystem)

	assert.Equal(t, 288957, score)
}
