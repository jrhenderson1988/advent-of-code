package d17

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

func Execute(input string) (days.Result, error) {
	target, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	highestY := CalculateHighestYPositionToStillLandInTarget(target)
	totalDistinctInitialVelocities := CalculateTotalDistinctInitialVelocities(target)

	return days.NewIntResult(highestY, totalDistinctInitialVelocities), nil
}

func CalculateHighestYPositionToStillLandInTarget(target targetArea) int {
	// when we shoot upwards and the probe peaks, the y coordinates of every step on the way down
	// are the same as they were on the way up. When the probe hits y=0, its current velocity will
	// be the negative value of the velocity that it went up, minus one. The step just before it
	// hits 0 will have the negative initial velocity. Since we always hit y=0 again, the maximum
	// velocity that we can shoot upwards, is one less than the absolute value of the bottom edge of
	// the target area. In the case of the example, the bottom edge is -10. So in order for the next
	// step to result in the probe landing with the same y as the bottom edge, the velocity when the
	// probe hits y=0 needs to be -10. Since the downward velocity is one more than the upward
	// velocity, we need to reduce the upward velocity by 1, so we fire it upwards with 9, which
	// results in a max y of: 9+8+7+6+5+4+3+2+1 = 45
	if target.bottomRight.y > 0 {
		panic("sorry, I couldn't be bothered to make this work for the case where the target area is above 0")
	}

	yVel := -target.bottomRight.y - 1
	y := 0
	for yVel > 0 {
		y += yVel
		yVel--
	}
	return y

}

func CalculateTotalDistinctInitialVelocities(target targetArea) int {
	if target.topLeft.x < 0 || target.bottomRight.y > 0 {
		panic("sorry, this only works when the target areas is below and to the right of 0, 0")
	}

	maxY := -target.bottomRight.y - 1
	minY := target.bottomRight.y
	maxX := target.bottomRight.x
	minX := target.topLeft.x

	for {
		x := maxXWithXVelocity(minX - 1)
		if x >= target.topLeft.x {
			minX--
		} else {
			break
		}
	}

	trajectories := make(map[coord]struct{}, 0)
	for yVel := common.MinInt(minY, maxY); yVel <= common.MaxInt(minY, maxY); yVel++ {
		for xVel := common.MinInt(minX, maxX); xVel <= common.MaxInt(minX, maxX); xVel++ {
			p := newProbe(0, 0, xVel, yVel)
			if p.launch(target) {
				trajectories[newCoord(xVel, yVel)] = struct{}{}
			}
		}
	}

	return len(trajectories)
}

func maxXWithXVelocity(xVel int) int {
	x := 0
	for xv := xVel; xv > 0; xv-- {
		x += xv
	}
	return x
}

func parseInput(input string) (targetArea, error) {
	input = strings.TrimSpace(input)
	if !strings.HasPrefix(input, "target area: ") {
		return targetArea{}, fmt.Errorf("input does not start with 'target area'")
	}

	input = input[13:]

	parts := strings.Split(input, ",")
	if len(parts) != 2 {
		return targetArea{}, fmt.Errorf("invalid input, expected x range and y range")
	}

	x1, x2, y1, y2 := 0, 0, 0, 0
	for _, part := range parts {
		part = strings.TrimSpace(part)
		innerParts := strings.Split(part, "=")
		if len(innerParts) != 2 {
			return targetArea{}, fmt.Errorf("expected coord range to be separated by =")
		}

		axis := innerParts[0]
		coords := strings.Split(innerParts[1], "..")
		if len(coords) != 2 {
			return targetArea{}, fmt.Errorf("expected coordinate range for axis %s", axis)
		}

		c1, err := common.StringToInt(coords[0])
		if err != nil {
			return targetArea{}, err
		}

		c2, err := common.StringToInt(coords[1])
		if err != nil {
			return targetArea{}, err
		}

		if axis == "x" {
			x1, x2 = c1, c2
		} else if axis == "y" {
			y1, y2 = c1, c2
		} else {
			return targetArea{}, fmt.Errorf("invalid axis")
		}
	}

	if x1 == 0 || x2 == 0 || y1 == 0 || y2 == 0 {
		return targetArea{}, fmt.Errorf("some coordinates are not set")
	}

	return newTargetArea(x1, x2, y1, y2), nil
}
