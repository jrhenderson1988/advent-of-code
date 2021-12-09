package d08

import (
	"fmt"
	"strings"
)

//type Signal []rune
type Signal map[rune]bool

func ParseSignal(input string) (Signal, error) {
	runes := make(map[rune]bool)
	for _, r := range []rune(strings.TrimSpace(input)) {
		if r < 'a' || r > 'g' {
			return nil, fmt.Errorf("invalid character %c in signal %s", r, input)
		}

		_, exists := runes[r]
		if exists {
			return nil, fmt.Errorf("character %c already exists", r)
		}

		runes[r] = true
	}

	return runes, nil
	//runes := []rune(strings.TrimSpace(input))
	//for _, r := range runes {
	//	if r < 'a' || r > 'g' {
	//		return nil, fmt.Errorf("invalid character %c in signal %s", r, input)
	//	}
	//}
	//return runes, nil
}

func (s Signal) Len() int {
	return len(s)
}

func (s Signal) Intersect(other Signal) Signal {
	newSignal := s.copy()
	for r := range other {
		newSignal[r] = true
	}

	return newSignal
}

func (s Signal) Diff(other Signal) Signal {
	newSignal := s.copy()
	for r := range other {
		_, exists := newSignal[r]
		if exists {
			delete(newSignal, r)
		}
	}

	return newSignal
}

func (s Signal) copy() Signal {
	newSignal := make(map[rune]bool)
	for r := range s {
		newSignal[r] = true
	}
	return newSignal
}

func (s Signal) Equals(other Signal) bool {
	return s.Len() == other.Len() && s.Contains(other)
}

func (s Signal) Contains(other Signal) bool {
	for r := range other {
		_, exists := s[r]
		if !exists {
			return false
		}
	}
	return true
}

func (s Signal) String() string {
	sb := strings.Builder{}
	for r := range s {
		sb.WriteRune(r)
	}
	return sb.String()
}
