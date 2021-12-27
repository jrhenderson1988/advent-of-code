package d21

type DeterministicDie struct {
	totalRolls int
	sides      int
}

func NewDeterministicDie(sides int) DeterministicDie {
	return DeterministicDie{sides: sides, totalRolls: 0}
}

func (dd DeterministicDie) Roll() (int, DeterministicDie) {
	value := (dd.totalRolls % dd.sides) + 1
	return value, DeterministicDie{totalRolls: dd.totalRolls + 1, sides: dd.sides}
}
