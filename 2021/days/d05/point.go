package d05

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Point struct {
	x int
	y int
}

func NewPoint(x, y int) Point {
	return Point{x, y}
}

func ParsePoint(input string) (Point, error) {
	input = strings.TrimSpace(input)
	parts := strings.Split(input, ",")
	if len(parts) != 2 {
		return Point{}, fmt.Errorf("expected exactly one X and one Y coordinate")
	}

	x, err := common.StringToInt(parts[0])
	if err != nil {
		return Point{}, err
	}

	y, err := common.StringToInt(parts[1])
	if err != nil {
		return Point{}, err
	}

	return NewPoint(x, y), nil
}

func (p Point) String() string {
	return fmt.Sprintf("(%d,%d)", p.x, p.y)
}
