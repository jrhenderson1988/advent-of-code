package d24

type MinMax struct {
	min uint64
	max uint64
}

type ALUState struct {
	registers [4]int32
	min       uint64
	max       uint64
}

func NewALUState(registers [4]int32, min, max uint64) ALUState {
	return ALUState{registers, min, max}
}

func (s ALUState) Copy() ALUState {
	return NewALUState(
		[4]int32{s.registers[0], s.registers[1], s.registers[2], s.registers[3]},
		s.min,
		s.max,
	)
}
