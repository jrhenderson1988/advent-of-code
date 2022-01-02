package d23

import "fmt"

type Amphipod int8

const (
	None Amphipod = -1
	A    Amphipod = 0
	B    Amphipod = 1
	C    Amphipod = 2
	D    Amphipod = 3
)

func AmphipodFromRune(ch rune) (Amphipod, error) {
	switch ch {
	case '.':
		return None, nil
	case 'A', 'a':
		return A, nil
	case 'B', 'b':
		return B, nil
	case 'C', 'c':
		return C, nil
	case 'D', 'd':
		return D, nil
	default:
		return None, fmt.Errorf("invalid amphipod rune: %c", ch)
	}
}

func (a Amphipod) EnergyCost() int {
	switch a {
	case None:
		panic("amphipod is 'none'")
	case A:
		return 1
	case B:
		return 10
	case C:
		return 100
	case D:
		return 1000
	default:
		panic("not an amphipod")
	}
}

func (a Amphipod) TargetRoom() int {
	switch a {
	case None:
		panic("amphipod is 'none'")
	case A, B, C, D:
		return int(a)
	default:
		panic("not an amphipod")
	}
}

func (a Amphipod) Label() rune {
	if a == None {
		return '.'
	}
	return rune(a) + 65
}
