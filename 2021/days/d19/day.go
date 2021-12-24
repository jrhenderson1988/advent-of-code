package d19

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
	"time"
)

func Execute(input string) (days.Result, error) {
	scanners, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalBeacons := CalculateTotalBeacons(scanners)
	//p2 := Part2(scanners)

	return days.NewIntResult(totalBeacons, -1), nil
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

func CalculateTotalBeacons(input []Scanner) int {
	scanners := input[:]
	if len(scanners) == 0 {
		panic("at least one scanner expected")
	} else if len(scanners) == 1 {
		return len(scanners[0].beacons)
	}

	// put the beacons of the first scanner into a "set" and remove the first scanner from the list
	beacons := make(map[Beacon]struct{})
	for _, b := range scanners[0].beacons {
		beacons[b] = struct{}{}
	}
	scanners = scanners[1:]

	iterations := 0
	for len(scanners) > 0 {

		for i := 0; i < len(scanners); i++ {
			before := timeInMillis()
			removeScanner := false
			scanner := scanners[i]
			for _, rotation := range scanner.Orientations() {
				if removeScanner {
					break
				}

				for beacon, _ := range beacons {
					if removeScanner {
						break
					}

					for _, otherBeacon := range rotation.beacons {
						if removeScanner {
							break
						}

						dist := beacon.Delta(otherBeacon)
						translated := make(map[Beacon]struct{})
						for _, rb := range rotation.beacons {
							translated[NewBeacon(rb.x+dist.x, rb.y+dist.y, rb.z+dist.z)] = struct{}{}
						}

						count := 0
						for b, _ := range beacons {
							_, contained := translated[b]
							if contained {
								count++
							}

							iterations++
							if count >= 12 {
								for tb, _ := range translated {
									beacons[tb] = struct{}{}
								}
								removeScanner = true
								break
							}
						}
					}
				}
			}

			if removeScanner {
				scanners[i] = scanners[len(scanners)-1]
				scanners = scanners[:len(scanners)-1]
			}

			after := timeInMillis()
			fmt.Printf("Scanner: %d, %dms\n", i, after - before)
		}
	}

	fmt.Println(iterations)

	return len(beacons)
}

func timeInMillis() int64 {
	return time.Now().UnixNano() / int64(time.Millisecond)
}

func Part2(input string) int {
	return -1
}
