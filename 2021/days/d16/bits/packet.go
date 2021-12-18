package bits

import (
	"fmt"
)

const (
	TypeIdSum         = 0
	TypeIdProduct     = 1
	TypeIdMinimum     = 2
	TypeIdMaximum     = 3
	TypeIdLiteral     = 4
	TypeIdGreaterThan = 5
	TypeIdLessThan    = 6
	TypeIdEqualTo     = 7
)

type Packet interface {
	GetVersion() int
	GetTypeId() int
	Evaluate() int
}

type LiteralPacket interface {
	Packet
}

type OperatorPacket interface {
	Packet
	GetChildren() []Packet
}

type literalPacket struct {
	version int
	value   int
}

func NewLiteralPacket(version, value int) LiteralPacket {
	return literalPacket{version, value}
}

func (lp literalPacket) GetVersion() int {
	return lp.version
}

func (lp literalPacket) GetTypeId() int {
	return TypeIdLiteral
}

func (lp literalPacket) Evaluate() int {
	return lp.value
}

func (lp literalPacket) String() string {
	return fmt.Sprintf("LiteralPacket(v%d -> %d)", lp.version, lp.value)
}

type operatorPacket struct {
	version  int
	typeId   int
	children []Packet
}

func NewOperatorPacket(version, typeId int, children []Packet) OperatorPacket {
	return operatorPacket{version, typeId, children}
}

func (op operatorPacket) GetVersion() int {
	return op.version
}

func (op operatorPacket) GetTypeId() int {
	return op.typeId
}

func (op operatorPacket) GetChildren() []Packet {
	return op.children
}

func (op operatorPacket) Evaluate() int {
	if len(op.children) == 0 {
		panic("no children")
	}

	children := op.GetChildren()

	switch op.typeId {
	case TypeIdSum:
		sum := children[0].Evaluate()
		for _, child := range children[1:] {
			sum += child.Evaluate()
		}
		return sum
	case TypeIdProduct:
		prod := children[0].Evaluate()
		for _, child := range children[1:] {
			prod *= child.Evaluate()
		}
		return prod
	case TypeIdMinimum:
		min := children[0].Evaluate()
		for _, child := range children[1:] {
			val := child.Evaluate()
			if val < min {
				min = val
			}
		}
		return min
	case TypeIdMaximum:
		max := children[0].Evaluate()
		for _, child := range children[1:] {
			val := child.Evaluate()
			if val > max {
				max = val
			}
		}
		return max
	case TypeIdGreaterThan:
		if len(children) != 2 {
			panic("expected 2 children for greater than operator packet")
		}
		if children[0].Evaluate() > children[1].Evaluate() {
			return 1
		} else {
			return 0
		}
	case TypeIdLessThan:
		if len(children) != 2 {
			panic("expected 2 children for less than operator packet")
		}
		if children[0].Evaluate() < children[1].Evaluate() {
			return 1
		} else {
			return 0
		}
	case TypeIdEqualTo:
		if len(children) != 2 {
			panic("expected 2 children for equal to operator packet")
		}
		if children[0].Evaluate() == children[1].Evaluate() {
			return 1
		} else {
			return 0
		}
	}
	return 0
}

func (op operatorPacket) String() string {
	return fmt.Sprintf("OperatorPacket(v%d, t%d, %s)", op.GetVersion(), op.GetTypeId(), op.GetChildren())
}
