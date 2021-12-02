package d02

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

type direction uint8

const (
	Up      = 1
	Down    = 2
	Forward = 3
)

type instruction struct {
	direction direction
	amount    int
}

func Execute(input string) (days.Result, error) {
	lines := common.SplitLines(input)
	instructions, err := parseInstructions(lines)
	if err != nil {
		return days.EmptyResult(), err
	}

	part1 := CalculateBasicDistanceFromOrigin(instructions)
	part2 := CalculateMultiplicativeDistanceFromOrigin(instructions)

	return days.NewResult(common.IntToString(part1), common.IntToString(part2)), nil
}

func CalculateBasicDistanceFromOrigin(instructions []instruction) int {
	x, y := 0, 0
	for _, instruction := range instructions {
		switch instruction.direction {
		case Up:
			y -= instruction.amount
		case Down:
			y += instruction.amount
		case Forward:
			x += instruction.amount
		}
	}

	return x * y
}

func CalculateMultiplicativeDistanceFromOrigin(instructions []instruction) int {
	x, y, aim := 0, 0, 0
	for _, instruction := range instructions {
		switch instruction.direction {
		case Up:
			aim -= instruction.amount
		case Down:
			aim += instruction.amount
		case Forward:
			x += instruction.amount
			y += instruction.amount * aim
		}
	}

	return x * y
}

func parseInstruction(line string) (instruction, error) {
	parts := strings.Split(strings.TrimSpace(line), " ")
	if len(parts) != 2 {
		return instruction{}, fmt.Errorf("unexpected number of parts: %d", len(parts))
	}

	var dir direction
	switch strings.ToLower(parts[0]) {
	case "forward":
		dir = Forward
	case "up":
		dir = Up
	case "down":
		dir = Down
	default:
		return instruction{}, fmt.Errorf("invalid direction: %s", parts[0])
	}

	amount, err := common.StringToInt(parts[1])
	if err != nil {
		return instruction{}, err
	}

	return instruction{direction: dir, amount: amount}, nil
}

func parseInstructions(lines []string) ([]instruction, error) {
	ins := make([]instruction, len(lines))
	for i, line := range lines {
		in, err := parseInstruction(line)
		if err != nil {
			return nil, err
		}
		ins[i] = in
	}
	return ins, nil
}
