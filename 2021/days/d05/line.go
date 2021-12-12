package d05

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Line struct {
	a Point
	b Point
}

func NewLine(a, b Point) Line {
	return Line{a, b}
}

func NewLineFromCoords(x1, y1, x2, y2 int) Line {
	return NewLine(NewPoint(x1, y1), NewPoint(x2, y2))
}

func ParseLine(input string) (Line, error) {
	input = strings.TrimSpace(input)
	parts := strings.Split(input, "->")
	if len(parts) != 2 {
		return Line{}, fmt.Errorf("expected exactly two values")
	}

	a, err := ParsePoint(parts[0])
	if err != nil {
		return Line{}, err
	}

	b, err := ParsePoint(parts[1])
	if err != nil {
		return Line{}, err
	}

	return NewLine(a, b), nil
}

func (l Line) IsHorizontal() bool {
	return l.a.y == l.b.y
}

func (l Line) IsVertical() bool {
	return l.a.x == l.b.x
}

func (l Line) String() string {
	return fmt.Sprintf("%s => %s", l.a, l.b)
}

func (l Line) PointsOnLine() []Point {
	points := make([]Point, 0)

	dx := l.b.x - l.a.x
	dy := l.b.y - l.a.y
	steps := common.MaxInt(common.AbsInt(dx), common.AbsInt(dy))
	for i := 0; i < steps; i++ {
		newX := float64(l.a.x) + ((float64(dx) / float64(steps)) * float64(i))
		if float64(int(newX)) != newX {
			continue
		}

		newY := float64(l.a.y) + ((float64(dy) / float64(steps)) * float64(i))
		if float64(int(newY)) != newY {
			continue
		}

		points = append(points, NewPoint(int(newX), int(newY)))
	}

	points = append(points, NewPoint(l.b.x, l.b.y))

	return points
}
