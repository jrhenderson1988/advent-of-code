package d24

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type InstructionKind int

const (
	inp InstructionKind = 1
	add InstructionKind = 2
	mul InstructionKind = 3
	div InstructionKind = 4
	mod InstructionKind = 5
	eql InstructionKind = 6
)

func InstructionKindFromString(ik string) InstructionKind {
	switch ik {
	case "inp":
		return inp
	case "add":
		return add
	case "mul":
		return mul
	case "div":
		return div
	case "mod":
		return mod
	case "eql":
		return eql
	default:
		panic(fmt.Sprintf("invalid instruction kind: %s", ik))
	}
}

type Register int

const (
	none Register = -1
	w    Register = 0
	x    Register = 1
	y    Register = 2
	z    Register = 3
)

func RegisterFromRune(ch rune) Register {
	switch ch {
	case 'w', 'W':
		return w
	case 'x', 'X':
		return x
	case 'y', 'Y':
		return y
	case 'z', 'Z':
		return z
	default:
		panic(fmt.Sprintf("invalid register: %c", ch))
	}
}

type Instruction struct {
	kind       InstructionKind
	a          Register
	bReg       Register
	bVal       int
	isRegister bool
}

func ParseInstruction(line string) (Instruction, error) {
	parts := strings.Split(strings.TrimSpace(line), " ")
	if len(parts) < 2 || len(parts) > 3 {
		return Instruction{}, fmt.Errorf("invalid instruction: %s", line)
	}

	instructionKind := InstructionKindFromString(parts[0])
	if len(parts[1]) != 1 {
		return Instruction{}, fmt.Errorf("invalid register specified in instruction: %s", line)
	}
	a := RegisterFromRune(rune(parts[1][0]))

	if instructionKind == inp {
		return Instruction{
			kind:       instructionKind,
			a:          a,
			bReg:       none,
			bVal:       -1,
			isRegister: true,
		}, nil
	}

	if len(parts) != 3 {
		return Instruction{}, fmt.Errorf("missing arguments for instruction: %s", line)
	}

	if len(parts[2]) == 1 && rune(parts[2][0]) >= 'w' && rune(parts[2][0]) <= 'z' {
		return Instruction{
			kind: instructionKind,
			a: a,
			bReg: RegisterFromRune(rune(parts[2][0])),
			bVal: -1,
			isRegister: true,
		}, nil
	} else {
		val, err := common.StringToInt(parts[2])
		if err != nil {
			return Instruction{}, err
		}

		return Instruction{
			kind:       instructionKind,
			a:          a,
			bReg:       none,
			bVal:       val,
			isRegister: false,
		}, nil
	}
}

type ALU struct {
	w            int
	x            int
	y            int
	z            int
	instructions []Instruction
}

func NewALU(w, x, y, z int, instructions []Instruction) *ALU {
	return &ALU{w, x, y, z, instructions}
}