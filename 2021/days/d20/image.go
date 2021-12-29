package d20

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

type Coord struct {
	x int
	y int
}

func newCoord(x, y int) Coord {
	return Coord{x, y}
}

type Image struct {
	lightPixels    map[Coord]struct{}
	topLeft        Coord
	bottomRight    Coord
	infiniteValues bool
}

func NewImage(lightPixels map[Coord]struct{}, infiniteValues bool) Image {
	first := true
	minX, minY, maxX, maxY := 0, 0, 0, 0
	for pos, _ := range lightPixels {
		if pos.x < minX || first {
			minX = pos.x
		}
		if pos.x > maxX || first {
			maxX = pos.x
		}
		if pos.y < minY || first {
			minY = pos.y
		}
		if pos.y > maxY || first {
			maxY = pos.y
		}
		first = false
	}

	topLeft := newCoord(minX, minY)
	bottomRight := newCoord(maxX, maxY)

	return Image{lightPixels, topLeft, bottomRight, infiniteValues}
}

func ParseImage(input string) (Image, error) {
	lightPixels := make(map[Coord]struct{})
	for y, line := range common.SplitLines(strings.TrimSpace(input)) {
		for x, ch := range []rune(strings.TrimSpace(line)) {
			if ch == '#' {
				lightPixels[newCoord(x, y)] = struct{}{}
			} else if ch != '.' {
				return Image{}, fmt.Errorf("unexpected character in image input: %c", ch)
			}
		}
	}
	return NewImage(lightPixels, false), nil
}

func (i Image) withinBounds(pt Coord) bool {
	return pt.x >= i.topLeft.x && pt.x <= i.bottomRight.x && pt.y >= i.topLeft.y && pt.y <= i.bottomRight.y
}

func (i Image) valueAt(coord Coord) int {
	deltas := []Coord{
		newCoord(-1, -1), newCoord(0, -1), newCoord(1, -1),
		newCoord(-1, 0), newCoord(0, 0), newCoord(1, 0),
		newCoord(-1, 1), newCoord(0, 1), newCoord(1, 1),
	}

	binary := make([]bool, len(deltas))
	for idx, delta := range deltas {
		pt := newCoord(coord.x+delta.x, coord.y+delta.y)
		if i.withinBounds(pt) {
			_, binary[idx] = i.lightPixels[pt]
		} else {
			binary[idx] = i.infiniteValues
		}
	}

	return int(common.BinarySliceToUint64(binary))
}

func (i Image) Apply(iha ImageEnhancementAlgorithm) Image {
	newImageLightPixels := make(map[Coord]struct{})
	for y := i.topLeft.y - 1; y <= i.bottomRight.y+1; y++ {
		for x := i.topLeft.x - 1; x <= i.bottomRight.x+1; x++ {
			pt := newCoord(x, y)
			idx := i.valueAt(pt)
			if iha.IsLight(idx) {
				newImageLightPixels[pt] = struct{}{}
			}
		}
	}

	newInfiniteValues := i.infiniteValues
	if iha.IsAlternating() {
		newInfiniteValues = !i.infiniteValues
	}

	return NewImage(newImageLightPixels, newInfiniteValues)
}

func (i Image) TotalLightPixels() int {
	if i.infiniteValues == true {
		panic("infinitely extending values are lit")
	}
	return len(i.lightPixels)
}

func (i Image) String() string {
	sb := strings.Builder{}
	for y := i.topLeft.y - 1; y < i.bottomRight.y+2; y++ {
		for x := i.topLeft.x - 1; x < i.bottomRight.x+2; x++ {
			_, present := i.lightPixels[newCoord(x, y)]
			if present {
				sb.WriteRune('#')
			} else {
				sb.WriteRune('.')
			}
		}
		sb.WriteRune('\n')
	}

	return sb.String()
}
