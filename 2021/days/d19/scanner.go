package d19

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Scanner struct {
	id           int
	orientations []map[Point]struct{}
}

func NewScanner(id int, points map[Point]struct{}) Scanner {
	return Scanner{id: id, orientations: createOrientations(points)}
}

func createOrientations(points map[Point]struct{}) []map[Point]struct{} {
	orientations := make([]map[Point]struct{}, 24)

	orientations[0] = transform(points, func(x, y, z int) (int, int, int) { return x, y, z })
	orientations[1] = transform(points, func(x, y, z int) (int, int, int) { return y, z, x })
	orientations[2] = transform(points, func(x, y, z int) (int, int, int) { return z, x, y })
	orientations[3] = transform(points, func(x, y, z int) (int, int, int) { return z, y, -x })
	orientations[4] = transform(points, func(x, y, z int) (int, int, int) { return y, x, -z })
	orientations[5] = transform(points, func(x, y, z int) (int, int, int) { return x, z, -y })

	orientations[6] = transform(points, func(x, y, z int) (int, int, int) { return x, -y, -z })
	orientations[7] = transform(points, func(x, y, z int) (int, int, int) { return y, -z, -x })
	orientations[8] = transform(points, func(x, y, z int) (int, int, int) { return z, -x, -y })
	orientations[9] = transform(points, func(x, y, z int) (int, int, int) { return z, -y, x })
	orientations[10] = transform(points, func(x, y, z int) (int, int, int) { return y, -x, z })
	orientations[11] = transform(points, func(x, y, z int) (int, int, int) { return x, -z, y })

	orientations[12] = transform(points, func(x, y, z int) (int, int, int) { return -x, y, -z })
	orientations[13] = transform(points, func(x, y, z int) (int, int, int) { return -y, z, -x })
	orientations[14] = transform(points, func(x, y, z int) (int, int, int) { return -z, x, -y })
	orientations[15] = transform(points, func(x, y, z int) (int, int, int) { return -z, y, x })
	orientations[16] = transform(points, func(x, y, z int) (int, int, int) { return -y, x, z })
	orientations[17] = transform(points, func(x, y, z int) (int, int, int) { return -x, z, y })

	orientations[18] = transform(points, func(x, y, z int) (int, int, int) { return -x, -y, z })
	orientations[19] = transform(points, func(x, y, z int) (int, int, int) { return -y, -z, x })
	orientations[20] = transform(points, func(x, y, z int) (int, int, int) { return -z, -x, y })
	orientations[21] = transform(points, func(x, y, z int) (int, int, int) { return -z, -y, -x })
	orientations[22] = transform(points, func(x, y, z int) (int, int, int) { return -y, -x, -z })
	orientations[23] = transform(points, func(x, y, z int) (int, int, int) { return -x, -z, -y })

	return orientations
}

type transformFunc func(x, y, z int) (int, int, int)

func transform(points map[Point]struct{}, transform transformFunc) map[Point]struct{} {
	transformations := make(map[Point]struct{}, len(points))
	for b := range points {
		x, y, z := transform(b.x, b.y, b.z)
		transformations[NewPoint(x, y, z)] = struct{}{}
	}
	return transformations
}

func ParseScanner(input string) (Scanner, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	if !strings.HasPrefix(lines[0], "--- scanner ") || !strings.HasSuffix(lines[0], "---") {
		return Scanner{}, fmt.Errorf("scanner chunk does not begin with identifier/heading: %s", lines[0])
	}

	id, err := common.StringToInt(lines[0][12 : len(lines[0])-4])
	if err != nil {
		return Scanner{}, err
	}

	points := make(map[Point]struct{}, 0)
	for _, line := range lines[1:] {
		pt, err := ParsePoint(line)
		if err != nil {
			return Scanner{}, err
		}
		points[pt] = struct{}{}
	}

	return NewScanner(id, points), nil
}

func (s Scanner) Points() map[Point]struct{} {
	return s.orientations[0]
}

func (s Scanner) Orientations() []map[Point]struct{} {
	return s.orientations
}
