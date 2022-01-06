package d24

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"math"
	"runtime"
	"sort"
	"strings"
	"time"
)

func Execute(input string) (days.Result, error) {
	alu, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	MaxModelNumber(alu.instructions)
	//MaxModelNumber2()

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

//func MaxModelNumber2() {
//	input := [14]int{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}
//	iterations := 0
//	start := timeInMillis()
//	for {
//		if runProgram(input) {
//			fmt.Println(input)
//			break
//		}
//
//		for i := len(input) - 1; i > 0; i-- {
//			if input[i] > 1 {
//				input[i]--
//				break
//			}
//			input[i] = 9
//		}
//
//		iterations++
//		if iterations%1000000000 == 0 {
//			fmt.Println("iterations: ", iterations, ", input: ", input, "time (ms): ", timeInMillis()-start)
//			start = timeInMillis()
//		}
//	}
//
//}

//func runProgram(input [14]int) bool {
//	ptr := 0
//
//	w, x, y, z := 0, 0, 0, 0
//
//	w = input[ptr]
//	ptr++
//	x = (z % 26) + 15
//	if x == w {
//		x = 0
//	} else {
//		x = 1
//	}
//	y = (25 * x) + 1
//	z = z * y
//	y = (w + 4) * x
//	z = z + y
//
//	w = input[ptr]
//	ptr++
//	x = (z % 26) + 14
//	if x == w {
//		x = 0
//	} else {
//		x = 1
//	}
//	y = (25 * x) + 1
//	z = z * y
//	y = (w + 16) * x
//	z = z + y
//
//	w = input[ptr]
//	ptr++
//	x = (z % 26) + 11
//	if x == w {
//		x = 0
//	} else {
//		x = 1
//	}
//	y = (25 * x) +  1
//	z = z * y
//	y = (w + 14) * x
//	z = z + y
//
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -13
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 3
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 1
//	x = x + 14
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 11
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 1
//	x = x + 15
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 13
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -7
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 11
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 1
//	x = x + 10
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 7
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -12
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 12
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 1
//	x = x + 15
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 15
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -16
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 13
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -9
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 1
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -8
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 15
//	y = y * x
//	z = z + y
//	w = input[ptr]
//	ptr++
//	x = x * 0
//	x = x + z
//	x = x % 26
//	z = z / 26
//	x = x + -8
//	if x == w {
//		x = 1
//	} else {
//		x = 0
//	}
//	if x == 0 {
//		x = 1
//	} else {
//		x = 0
//	}
//	y = y * 0
//	y = y + 25
//	y = y * x
//	y = y + 1
//	z = z * y
//	y = y * 0
//	y = y + w
//	y = y + 4
//	y = y * x
//	z = z + y
//
//	return z == 0
//}

func MaxModelNumber(program []Instruction) {
	state := []ALUState{NewALUState([...]int32{0, 0, 0, 0}, 0, 0)}
	for n, instruction := range program {
		start := timeInMillis()
		switch instruction.kind {
		case inp:
			for j, _ := range state {
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

			//dedup := make(map[[4]int]MinMax)
			//for _, entry := range state {
			//	e, exists := dedup[entry.registers]
			//	if !exists {
			//		dedup[entry.registers] = MinMax{min: entry.min, max: entry.max}
			//	} else {
			//		dedup[entry.registers] = MinMax{min: min(entry.min, e.min), max: max(entry.max, e.max)}
			//	}
			//}
			//
			//state = make([]ALUState, len(dedup))
			//j := 0
			//for registers, minmax := range dedup {
			//	state[j] = NewALUState(registers, minmax.min, minmax.max)
			//	j++
			//}

			nextState := make([]ALUState, 0)
			for _, entry := range state {
				for j := 1; j <= 9; j++ {
					newState := entry.Copy()
					newState.registers[instruction.a] = int32(j)
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

		end := timeInMillis()
		var m runtime.MemStats
		runtime.ReadMemStats(&m)
		fmt.Printf("instruction %d (%s) of %d, total states: %d, time: %dms, allocated: %v MiB\n", n+1, instruction.String(), len(program), len(state), end-start, bToMb(m.Alloc))
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

	fmt.Printf("Largest: %d, Smallest: %d\n", largest, smallest)
}

func registerValueOrLiteral(instruction Instruction, registers [4]int32) int32 {
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

func bToMb(b uint64) uint64 {
	return b / 1024 / 1024
}
