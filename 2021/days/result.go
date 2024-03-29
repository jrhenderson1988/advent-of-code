package days

import (
	"aoc2021/common"
	"fmt"
)

type Result struct {
	Part1 string
	Part2 string
}

func NewResult(part1, part2 string) Result {
	return Result{Part1: part1, Part2: part2}
}

func NewIntResult(part1, part2 int) Result {
	return NewResult(common.IntToString(part1), common.IntToString(part2))
}

func EmptyResult() Result {
	return Result{}
}

func (r Result) String() string {
	return fmt.Sprintf("Part 1: %s\nPart 2: %s", r.Part1, r.Part2)
}
