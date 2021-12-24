package d19

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Point struct {
	x int
	y int
	z int
}

func NewPoint(x, y, z int) Point {
	return Point{x, y, z}
}

func ParsePoint(input string) (Point, error) {
	parts := strings.Split(strings.TrimSpace(input), ",")
	if len(parts) != 3 {
		return Point{}, fmt.Errorf("invalid point, expected 3 parts")
	}

	coords, err := common.StringsToInts(parts)
	if err != nil {
		return Point{}, err
	}

	return NewPoint(coords[0], coords[1], coords[2]), nil
}

func (b Point) Distance(other Point) int {
	return common.AbsInt(b.x-other.x) + common.AbsInt(b.y-other.y) + common.AbsInt(b.z-other.z)
}

func (b Point) Delta(other Point) Point {
	return NewPoint(b.x - other.x, b.y - other.y, b.z - other.z)
}

func (b Point) Subtract(other Point) Point {
	return NewPoint(b.x-other.x, b.y-other.y, b.z-other.z)
}

func (b Point) String() string {
	return fmt.Sprintf("%d,%d,%d", b.x, b.y, b.z)
}
