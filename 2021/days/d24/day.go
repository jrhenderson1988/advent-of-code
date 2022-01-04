package d24

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"math"
	"strings"
	"time"
)

func Execute(input string) (days.Result, error) {
	alu, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	MaxModelNumber(alu.instructions)

	return days.NewIntResult(-1, -1), nil
}

func parseInput(input string) (*ALU, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	instructions := make([]Instruction, len(lines))
	for i, line := range lines {
		instruction, err := ParseInstruction(line)
		if err != nil {
			return nil, err
		}
		instructions[i] = instruction
	}
	return NewALU(0, 0, 0, 0, instructions), nil
}

func MaxModelNumber(program []Instruction) {
	state := []ALUState{NewALUState([...]int{0, 0, 0, 0}, 0, 0)}
	for i, instruction := range program {
		start := timeInMillis()
		switch instruction.kind {
		case inp:
			dedup := make(map[[4]int]MinMax)
			for _, entry := range state {
				e, exists := dedup[entry.registers]
				if !exists {
					dedup[entry.registers] = MinMax{min: entry.min, max: entry.max}
				} else {
					dedup[entry.registers] = MinMax{min: min(entry.min, e.min), max: max(entry.max, e.max)}
				}
			}

			state = make([]ALUState, len(dedup))
			j := 0
			for registers, minmax := range dedup {
				state[j] = NewALUState(registers, minmax.min, minmax.max)
				j++
			}

			//stateLen := len(state)
			//nextState := make([]ALUState, stateLen*9)
			nextState := make([]ALUState, 0)
			for _, entry := range state {
				for j := 1; j <= 9; j++ {
					newState := entry.Copy()
					newState.registers[instruction.a] = j
					newState.min = entry.min*uint64(10) + uint64(j)
					newState.max = entry.max*uint64(10) + uint64(j)
					nextState = append(nextState, newState)
					//nextState[(idx*stateLen)+j-1] = newState
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
		end := timeInMillis()
		fmt.Printf("instruction %d of %d, total states: %d, time: %dms\n", i, len(program), len(state), end-start)
	}

	var largest uint64 = 0
	var smallest uint64 = math.MaxUint64
	for _, item := range state {
		if item.registers[3] == 0 {
			if item.max > largest {
				largest = item.max
			}
			if item.min < smallest {
				smallest = item.min
			}
		}
	}

	fmt.Printf("Largest: %d, Smallest: %d\n", largest, smallest)
}

func registerValueOrLiteral(instruction Instruction, registers [4]int) int {
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

func timeInMillis() int64 {
	return time.Now().UnixNano() / int64(time.Millisecond)
}
