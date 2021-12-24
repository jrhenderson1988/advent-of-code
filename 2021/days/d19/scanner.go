package d19

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Scanner struct {
	id      int
	beacons []Beacon
}

func NewScanner(id int, beacons []Beacon) Scanner {
	return Scanner{id, beacons}
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

	beacons := make([]Beacon, 0)
	for _, line := range lines[1:] {
		beacon, err := ParseBeacon(line)
		if err != nil {
			return Scanner{}, err
		}
		beacons = append(beacons, beacon)
	}

	return NewScanner(id, beacons), nil
}

func (s Scanner) Orientations() []Scanner {
	scanners := make([]Scanner, 24)

	scanners[0] = s.transform(func(x, y, z int) (int, int, int) { return x, y, z })
	scanners[1] = s.transform(func(x, y, z int) (int, int, int) { return y, z, x })
	scanners[2] = s.transform(func(x, y, z int) (int, int, int) { return z, x, y })
	scanners[3] = s.transform(func(x, y, z int) (int, int, int) { return z, y, -x })
	scanners[4] = s.transform(func(x, y, z int) (int, int, int) { return y, x, -z })
	scanners[5] = s.transform(func(x, y, z int) (int, int, int) { return x, z, -y })

	scanners[6] = s.transform(func(x, y, z int) (int, int, int) { return x, -y, -z })
	scanners[7] = s.transform(func(x, y, z int) (int, int, int) { return y, -z, -x })
	scanners[8] = s.transform(func(x, y, z int) (int, int, int) { return z, -x, -y })
	scanners[9] = s.transform(func(x, y, z int) (int, int, int) { return z, -y, x })
	scanners[10] = s.transform(func(x, y, z int) (int, int, int) { return y, -x, z })
	scanners[11] = s.transform(func(x, y, z int) (int, int, int) { return x, -z, y })

	scanners[12] = s.transform(func(x, y, z int) (int, int, int) { return -x, y, -z })
	scanners[13] = s.transform(func(x, y, z int) (int, int, int) { return -y, z, -x })
	scanners[14] = s.transform(func(x, y, z int) (int, int, int) { return -z, x, -y })
	scanners[15] = s.transform(func(x, y, z int) (int, int, int) { return -z, y, x })
	scanners[16] = s.transform(func(x, y, z int) (int, int, int) { return -y, x, z })
	scanners[17] = s.transform(func(x, y, z int) (int, int, int) { return -x, z, y })

	scanners[18] = s.transform(func(x, y, z int) (int, int, int) { return -x, -y, z })
	scanners[19] = s.transform(func(x, y, z int) (int, int, int) { return -y, -z, x })
	scanners[20] = s.transform(func(x, y, z int) (int, int, int) { return -z, -x, y })
	scanners[21] = s.transform(func(x, y, z int) (int, int, int) { return -z, -y, -x })
	scanners[22] = s.transform(func(x, y, z int) (int, int, int) { return -y, -x, -z })
	scanners[23] = s.transform(func(x, y, z int) (int, int, int) { return -x, -z, -y })

	return scanners
}

type transformFunc func(x, y, z int) (int, int, int)

func (s Scanner) transform(transform transformFunc) Scanner {
	beacons := make([]Beacon, len(s.beacons))
	for i, b := range s.beacons {
		x, y, z := transform(b.x, b.y, b.z)
		beacons[i] = NewBeacon(x, y, z)
	}
	return NewScanner(s.id, beacons)
}

func (s Scanner) Equals(other Scanner) bool {
	if s.id != other.id {
		return false
	}

	for i, b := range s.beacons {
		if b.x != other.beacons[i].x || b.y != other.beacons[i].y || b.z != other.beacons[i].z {
			return false
		}
	}

	return true
}

func (s Scanner) String() string {
	sb := strings.Builder{}
	for _, b := range s.beacons {
		sb.WriteRune('\n')
		sb.WriteString(b.String())
	}
	return fmt.Sprintf("--- scanner %d ---\n%s\n\n", s.id, sb.String())
}
