package d18

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() []*Number {
	input := "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
		"[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
		"[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
		"[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
		"[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
		"[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
		"[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
		"[[9,3],[[9,9],[6,[4,9]]]]\n" +
		"[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
		"[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"

	numbers, err := parseInput(input)
	if err != nil {
		panic(err)
	}
	return numbers
}

func TestCalculateMagnitudeOfFinalSum(t *testing.T) {
	numbers := getTestInput()
	assert.Equal(t, 4140, CalculateMagnitudeOfFinalSum(numbers))
}

func TestLargestMagnitudeFromTwoNumbers(t *testing.T) {
	numbers := getTestInput()
	assert.Equal(t, 3993, LargestMagnitudeFromTwoNumbers(numbers))
}
