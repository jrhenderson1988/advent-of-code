package d18

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Number struct {
	left   *Number
	right  *Number
	value  int
	parent *Number
}

func ParseNumber(input string) (*Number, error) {
	return parseNumber(nil, input)
}

func parseNumber(parent *Number, input string) (*Number, error) {
	input = strings.TrimSpace(input)
	if input[0] == '[' {
		if input[len(input)-1] != ']' {
			return nil, fmt.Errorf("number pair starts with but does not end with '[': %s", input)
		}

		commaPos := -1

		stack := 0
		for i, ch := range input[1 : len(input)-1] {
			if ch == '[' {
				stack++
			} else if ch == ']' {
				stack--
			} else if ch == ',' && stack == 0 {
				commaPos = i
				break
			}
		}

		if commaPos == -1 {
			return nil, fmt.Errorf("could not find separating comma (input: %s)", input)
		}

		pair := &Number{parent: parent, left: nil, right: nil}

		left, err := parseNumber(pair, input[1:commaPos+1])
		if err != nil {
			return nil, err
		}
		pair.left = left

		right, err := parseNumber(pair, input[commaPos+2:len(input)-1])
		if err != nil {
			return nil, err
		}
		pair.right = right

		return pair, nil
	}

	literal, err := common.StringToInt(input)
	if err != nil {
		return nil, err
	}

	return &Number{parent: parent, left: nil, right: nil, value: literal}, nil
}

func (n *Number) IsRoot() bool {
	return n.parent == nil
}

func (n *Number) IsNode() bool {
	return n.left != nil
}

func (n *Number) IsLiteral() bool {
	return !n.IsNode()
}

func (n *Number) Add(other *Number) *Number {
	newNum := &Number{parent: nil, left: n.Copy(), right: other.Copy()}
	return newNum.Reduce()
}

func (n *Number) Reduce() *Number {
	cpy := n.Copy()

	for {
		hasExploded := explode(cpy, 0, false)
		if hasExploded {
			continue
		}

		hasSplit := split(cpy, false)
		if hasSplit {
			continue
		}

		break
	}

	return cpy
}

func (n *Number) Magnitude() int {
	if n.IsLiteral() {
		return n.value
	}

	return n.left.Magnitude()*3 + n.right.Magnitude()*2
}

func (n *Number) Copy() *Number {
	newNum := &Number{parent: nil, left: nil, right: nil, value: n.value}
	if n.IsNode() {
		left := n.left.Copy()
		left.parent = newNum
		newNum.left = left

		right := n.right.Copy()
		right.parent = newNum
		newNum.right = right
	}
	return newNum
}

func (n *Number) String() string {
	if n.IsNode() {
		return fmt.Sprintf("[%s,%s]", n.left.String(), n.right.String())
	} else {
		return fmt.Sprintf("%d", n.value)
	}
}

func explode(num *Number, depth int, exploded bool) bool {
	if exploded {
		return exploded
	}

	if num.IsNode() && depth == 4 {
		addToNearest(num, true)
		addToNearest(num, false)

		zero := &Number{parent: num.parent, left: nil, right: nil, value: 0}
		if num.parent.left == num {
			num.parent.left = zero
		} else {
			num.parent.right = zero
		}

		return true
	} else if num.IsNode() && depth < 4 {
		exploded = explode(num.left, depth+1, false)
		if exploded {
			return true
		}

		exploded = explode(num.right, depth+1, false)
		if exploded {
			return true
		}
	}

	return false
}

func addToNearest(num *Number, left bool) {
	n := num
	for !n.IsRoot() {
		p := n.parent

		var child *Number
		if left {
			child = p.left
		} else {
			child = p.right
		}

		if child != n {
			n = child

			for n.IsNode() {
				if left {
					n = n.right
				} else {
					n = n.left
				}
			}

			if left {
				n.value += num.left.value
			} else {
				n.value += num.right.value
			}

			break
		}

		n = p
	}
}

func split(num *Number, hasSplit bool) bool {
	if hasSplit {
		return hasSplit
	}

	if num.IsLiteral() && num.value >= 10 {
		newNumber := &Number{parent: num.parent, left: nil, right: nil}
		left := &Number{parent: newNumber, left: nil, right: nil, value: num.value / 2}
		right := &Number{parent: newNumber, left: nil, right: nil, value: num.value / 2}
		if num.value%2 == 1 {
			right.value += 1
		}
		newNumber.left = left
		newNumber.right = right

		if num.parent.left == num {
			num.parent.left = newNumber
		} else {
			num.parent.right = newNumber
		}

		return true
	} else if num.IsNode() {
		hasSplit = split(num.left, false)
		if hasSplit {
			return true
		}

		hasSplit = split(num.right, false)
		if hasSplit {
			return true
		}
	}

	return false
}
