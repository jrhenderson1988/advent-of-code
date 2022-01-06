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

func (k InstructionKind) String() string {
	switch k {
	case inp:
		return "inp"
	case add:
		return "add"
	case mul:
		return "mul"
	case div:
		return "div"
	case mod:
		return "mod"
	case eql:
		return "eql"
	default:
		panic("invalid kind")
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

func (r Register) String() string {
	switch r {
	case w:
		return "w"
	case x:
		return "x"
	case y:
		return "y"
	case z:
		return "z"
	default:
		panic("invalid register")
	}
}

type Instruction struct {
	kind       InstructionKind
	a          Register
	bReg       Register
	bVal       int32
	isRegister bool
}

func (i Instruction) String() string {
	sb := strings.Builder{}
	sb.WriteString(i.kind.String())
	sb.WriteString(" ")
	sb.WriteString(i.a.String())
	sb.WriteString(" ")
	if i.kind == inp {
		return sb.String()
	}
	if i.isRegister {
		sb.WriteString(i.bReg.String())
	} else {
		sb.WriteString(fmt.Sprintf("%d", i.bVal))
	}
	return sb.String()
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
			kind:       instructionKind,
			a:          a,
			bReg:       RegisterFromRune(rune(parts[2][0])),
			bVal:       -1,
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
			bVal:       int32(val),
			isRegister: false,
		}, nil
	}
}

type ALU struct {
	registers    [4]int32
	instructions []Instruction
}

func NewALU(w, x, y, z int32, instructions []Instruction) *ALU {
	return &ALU{[4]int32{w, x, y, z}, instructions}
}

func (a *ALU) Execute(input [14]int32) {
	ptr := 0
	for _, instruction := range a.instructions {
		switch instruction.kind {
		case inp:
			a.registers[instruction.a] = input[ptr]
			ptr++
		case add:
			a.registers[instruction.a] += a.referenceOrLiteral(instruction)
		case mul:
			a.registers[instruction.a] *= a.referenceOrLiteral(instruction)
		case div:
			a.registers[instruction.a] /= a.referenceOrLiteral(instruction)
		case mod:
			a.registers[instruction.a] %= a.referenceOrLiteral(instruction)
		case eql:
			if a.registers[instruction.a] == a.referenceOrLiteral(instruction) {
				a.registers[instruction.a] = 1
			} else {
				a.registers[instruction.a] = 0
			}
		}
	}
}

func (a *ALU) referenceOrLiteral(instruction Instruction) int32 {
	if instruction.isRegister {
		return a.registers[instruction.bReg]
	}
	return instruction.bVal
}

func (a *ALU) String() string {
	return fmt.Sprintf("ALU[%d, %d, %d, %d]", a.registers[0], a.registers[1], a.registers[2], a.registers[3])
}
