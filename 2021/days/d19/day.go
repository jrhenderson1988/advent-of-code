package d19

import (
	"aoc2021/common"
	"aoc2021/days"
	"strings"
)

func Execute(input string) (days.Result, error) {
	scanners, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	beacons, deltas := FindAllBeaconsAndDeltas(scanners)
	totalBeacons := TotalBeacons(beacons)
	maximumDistance := MaxManhattanDistanceBetweenScanners(deltas)

	return days.NewIntResult(totalBeacons, maximumDistance), nil
}

func parseInput(input string) ([]Scanner, error) {
	scanners := make([]Scanner, 0)
	for _, s := range strings.Split(common.NormalizeLineBreaks(strings.TrimSpace(input)), "\n\n") {
		scanner, err := ParseScanner(s)
		if err != nil {
			return nil, err
		}
		scanners = append(scanners, scanner)
	}
	return scanners, nil
}

func TotalBeacons(beacons map[Point]struct{}) int {
	return len(beacons)
}

func MaxManhattanDistanceBetweenScanners(deltas []Point) int {
	maxDist := 0
	for i := 0; i < len(deltas); i++ {
		for j := 0; j < len(deltas); j++ {
			if i == j {
				continue
			}

			distance := deltas[i].Distance(deltas[j])
			if distance > maxDist {
				maxDist = distance
			}
		}
	}

	return maxDist
}

func FindAllBeaconsAndDeltas(input []Scanner) (map[Point]struct{}, []Point) {
	scanners := input[:]
	if len(scanners) < 2 {
		panic("at least two scanners expected")
	}

	beacons := scanners[0].Points()
	deltas := []Point{NewPoint(0, 0, 0)}

	for len(scanners) > 0 {
		for i := 0; i < len(scanners); i++ {
			removeScanner := false
			for _, orientation := range scanners[i].Orientations() {
				if removeScanner {
					break
				}

				for _, prod := range product(beacons, orientation) {
					delta := prod.a.Delta(prod.b)
					translated := translateBeacons(orientation, delta)
					if containsAtLeast(beacons, translated, 12) {
						beacons = mergeBeacons(beacons, translated)
						deltas = append(deltas, delta)
						removeScanner = true
						break
					}
				}
			}

			if removeScanner {
				scanners[i] = scanners[len(scanners)-1]
				scanners = scanners[:len(scanners)-1]
			}
		}
	}

	return beacons, deltas
}

func translateBeacons(beacons map[Point]struct{}, delta Point) map[Point]struct{} {
	translated := make(map[Point]struct{})
	for b, _ := range beacons {
		translated[NewPoint(b.x+delta.x, b.y+delta.y, b.z+delta.z)] = struct{}{}
	}
	return translated
}

func containsAtLeast(beacons map[Point]struct{}, others map[Point]struct{}, atLeast int) bool {
	count := 0
	for b, _ := range others {
		_, contained := beacons[b]
		if contained {
			count++
		}

		if count >= atLeast {
			return true
		}
	}
	return false
}

type pair struct {
	a Point
	b Point
}

func product(a map[Point]struct{}, b map[Point]struct{}) []pair {
	prod := make([]pair, len(a) * len(b))
	i := 0
	for ai := range a {
		for bi := range b {
			prod[i] = pair{a: ai, b: bi}
			i++
		}
	}
	return prod
}

func mergeBeacons(beacons map[Point]struct{}, toAppend map[Point]struct{}) map[Point]struct{} {
	newBeacons := make(map[Point]struct{})
	for b := range beacons {
		newBeacons[b] = struct{}{}
	}
	for b := range toAppend {
		newBeacons[b] = struct{}{}
	}
	return newBeacons
}
