package d17

import "aoc2021/common"

type targetArea struct {
	topLeft     coord
	bottomRight coord
}

func newTargetArea(x1, x2, y1, y2 int) targetArea {
	maxX := common.MaxInt(x1, x2)
	minX := common.MinInt(x1, x2)
	maxY := common.MaxInt(y1, y2)
	minY := common.MinInt(y1, y2)

	return targetArea{
		topLeft:     newCoord(minX, maxY),
		bottomRight: newCoord(maxX, minY),
	}
}

func (ta targetArea) Contains(p probe) bool {
	return p.pos.x >= ta.topLeft.x && p.pos.x <= ta.bottomRight.x && p.pos.y <= ta.topLeft.y && p.pos.y >= ta.bottomRight.y
}

func (ta targetArea) Missed(p probe) bool {
	if p.pos.x > ta.bottomRight.x {
		// we've overshot horizontally
		return true
	} else if p.pos.x < ta.topLeft.x && p.xVel == 0 {
		// we've undershot horizontally
		return true
	} else if p.pos.y < ta.bottomRight.y {
		// we've gone past the bottom
		return true
	}

	return false
}
