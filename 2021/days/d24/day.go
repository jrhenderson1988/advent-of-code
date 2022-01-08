package d24

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"math"
	"sort"
	"strings"
)

// The approach for the solution came from: https://www.mattkeeter.com/blog/2021-12-27-brute/
// The primary adjustments made were to reduce memory usage by splitting it up 9 ways and taking the
// minimum and maximum of all of those 9 sets of states.
func Execute(input string) (days.Result, error) {
	instructions, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	smallest, largest := MaxModelNumber(instructions)

	return days.NewResult(fmt.Sprintf("%d", largest), fmt.Sprintf("%d", smallest)), nil
}

func parseInput(input string) ([]Instruction, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	instructions := make([]Instruction, len(lines))
	for i, line := range lines {
		instruction, err := ParseInstruction(line)
		if err != nil {
			return nil, err
		}
		instructions[i] = instruction
	}
	return instructions, nil
}

func MaxModelNumber(program []Instruction) (uint64, uint64) {
	var smallest, largest uint64 = math.MaxUint64, 0
	for i := 1; i <= 9; i++ {
		minModelNum, maxModelNum := maxModelNumber(program, int64(i))
		if minModelNum < smallest {
			smallest = minModelNum
		}
		if maxModelNum > largest {
			largest = maxModelNum
		}
	}

	return smallest, largest
}

func maxModelNumber(program []Instruction, first int64) (uint64, uint64) {
	state := []ALUState{NewALUState([...]int64{0, 0, 0, 0}, 0, 0)}
	firstDone := false

	for _, instruction := range program {
		switch instruction.kind {
		case inp:
			if !firstDone {
				state[0].registers[instruction.a] = first
				state[0].min = state[0].min*uint64(10) + uint64(first)
				state[0].max = state[0].max*uint64(10) + uint64(first)

				firstDone = true
				continue
			}

			for j := range state {
				state[j].registers[instruction.a] = 0
			}

			sort.Slice(state, func(a, b int) bool {
				for j := 0; j < 4; j++ {
					if state[a].registers[j] < state[b].registers[j] {
						return true
					}
				}
				return false
			})

			i := 0
			for j := 1; j < len(state); j++ {
				if state[i].registers == state[j].registers {
					imin, imax := state[i].min, state[i].max
					jmin, jmax := state[j].min, state[j].max
					state[i].min, state[i].max = min(imin, jmin), max(imax, jmax)
				} else {
					i += 1
					state[i] = state[j]
				}
			}

			state = state[:i+1]

			nextState := make([]ALUState, 0)
			for _, entry := range state {
				for j := 1; j <= 9; j++ {
					newState := entry.Copy()
					newState.registers[instruction.a] = int64(j)
					newState.min = entry.min*uint64(10) + uint64(j)
					newState.max = entry.max*uint64(10) + uint64(j)
					nextState = append(nextState, newState)
				}
			}
			state = nextState
		case add:
			for j, entry := range state {
				a := entry.registers[instruction.a]
				b := registerValueOrLiteral(instruction, entry.registers)
				state[j].registers[instruction.a] = a + b
			}
		case mul:
			for j, entry := range state {
				a := entry.registers[instruction.a]
				b := registerValueOrLiteral(instruction, entry.registers)
				state[j].registers[instruction.a] = a * b
			}
		case div:
			for j, entry := range state {
				a := entry.registers[instruction.a]
				b := registerValueOrLiteral(instruction, entry.registers)
				state[j].registers[instruction.a] = a / b
			}
			break
		case mod:
			for j, entry := range state {
				a := entry.registers[instruction.a]
				b := registerValueOrLiteral(instruction, entry.registers)
				state[j].registers[instruction.a] = a % b
			}
		case eql:
			for j, entry := range state {
				a := entry.registers[instruction.a]
				b := registerValueOrLiteral(instruction, entry.registers)
				if a == b {
					state[j].registers[instruction.a] = 1
				} else {
					state[j].registers[instruction.a] = 0
				}
			}
		default:
			panic("invalid instruction")
		}
	}

	var largest uint64 = 0
	var smallest uint64 = math.MaxUint64
	for _, item := range state {
		if item.registers[z] == 0 {
			if item.max > largest {
				largest = item.max
			}
			if item.min < smallest {
				smallest = item.min
			}
		}
	}

	return smallest, largest
}

func registerValueOrLiteral(instruction Instruction, registers [4]int64) int64 {
	if instruction.isRegister {
		return registers[instruction.bReg]
	}
	return instruction.bVal
}

func min(a, b uint64) uint64 {
	if a < b {
		return a
	}
	return b
}

func max(a, b uint64) uint64 {
	if a > b {
		return a
	}
	return b
}
