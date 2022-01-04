package d24

import (
	"fmt"
	"testing"
)

func TestPart1(t *testing.T) {
	input := "inp w\nadd z w\nmod z 2\ndiv w 2\nadd y w\nmod y 2\ndiv w 2\nadd x w\nmod x 2\ndiv w 2\nmod w 2"
	alu, err := parseInput(input)
	if err != nil {
		panic(err)
	}

	fmt.Println(alu)
}