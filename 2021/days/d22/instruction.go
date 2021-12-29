package d22

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type coordRange struct {
	from int
	to   int
}

func NewCoordRange(from, to int) coordRange {
	return coordRange{from, to}
}

func ParseCoordRange(input string) (coordRange, error) {
	parts := strings.Split(strings.TrimSpace(input), "..")
	if len(parts) != 2 {
		return coordRange{}, fmt.Errorf("expected syntax: <from>..<to>")
	}
	values, err := common.StringsToInts(parts)
	if err != nil {
		return coordRange{}, err
	}
	from := common.MinInt(values[0], values[1])
	to := common.MaxInt(values[0], values[1])
	return NewCoordRange(from, to), nil
}

type Instruction struct {
	on bool
	x  coordRange
	y  coordRange
	z  coordRange
}

func NewInstruction(on bool, x, y, z coordRange) Instruction {
	return Instruction{on, x, y, z}
}

func ParseInstruction(input string) (Instruction, error) {
	input = strings.TrimSpace(input)

	on, start := false, -1
	if strings.HasPrefix(input, "on") {
		start = 2
		on = true
	} else if strings.HasPrefix(input, "off") {
		start = 3
		on = false
	} else {
		return Instruction{}, fmt.Errorf("instruction must begin with off or on")
	}

	var x, y, z coordRange
	var totalSet uint8 = 0

	rest := input[start:]
	for _, coordRangeChunk := range strings.Split(rest, ",") {
		coordRangeChunk = strings.TrimSpace(coordRangeChunk)
		if coordRangeChunk[1] != '=' {
			return Instruction{}, fmt.Errorf("invalid coordinate range chunk: %s", coordRangeChunk)
		}

		var err error
		if coordRangeChunk[0] == 'x' {
			x, err = ParseCoordRange(coordRangeChunk[2:])
			totalSet |= 4
		} else if coordRangeChunk[0] == 'y' {
			y, err = ParseCoordRange(coordRangeChunk[2:])
			totalSet |= 2
		} else if coordRangeChunk[0] == 'z' {
			z, err = ParseCoordRange(coordRangeChunk[2:])
			totalSet |= 1
		}

		if err != nil {
			return Instruction{}, err
		}
	}

	if totalSet != 7 {
		return Instruction{}, fmt.Errorf("one or more coordinate ranges were not specified")
	}

	return NewInstruction(on, x, y, z), nil
}
