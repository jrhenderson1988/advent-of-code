package d25

import "fmt"

type cucumber int

const (
	none  cucumber = 0
	east  cucumber = 1
	south cucumber = 2
)

func parseCucumber(ch rune) (cucumber, error) {
	switch ch {
	case '>':
		return east, nil
	case 'v', 'V':
		return south, nil
	case '.':
		return none, nil
	default:
		return -1, fmt.Errorf("invalid cucumber: %c", ch)
	}
}
