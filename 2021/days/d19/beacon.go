package d19

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Beacon struct {
	x int
	y int
	z int
}

func NewBeacon(x, y, z int) Beacon {
	return Beacon{x, y, z}
}

func ParseBeacon(input string) (Beacon, error) {
	parts := strings.Split(strings.TrimSpace(input), ",")
	if len(parts) != 3 {
		return Beacon{}, fmt.Errorf("invalid beacon, expected 3 parts")
	}

	coords, err := common.StringsToInts(parts)
	if err != nil {
		return Beacon{}, err
	}

	return NewBeacon(coords[0], coords[1], coords[2]), nil
}

func (b Beacon) Distance(other Beacon) int {
	return common.AbsInt(b.x-other.x) + common.AbsInt(b.y-other.y) + common.AbsInt(b.z-other.z)
}

func (b Beacon) Delta(other Beacon) Beacon {
	return NewBeacon(b.x - other.x, b.y - other.y, b.z - other.z)
}

func (b Beacon) Subtract(other Beacon) Beacon {
	return NewBeacon(b.x-other.x, b.y-other.y, b.z-other.z)
}

func (b Beacon) String() string {
	return fmt.Sprintf("%d,%d,%d", b.x, b.y, b.z)
}
