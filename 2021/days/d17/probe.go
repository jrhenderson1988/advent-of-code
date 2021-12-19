package d17

import "fmt"

type probe struct {
	start coord
	pos   coord
	xVel  int
	yVel  int
}

func newProbe(x, y, xVel, yVel int) probe {
	return probe{
		start: newCoord(x, y),
		pos:   newCoord(x, y),
		xVel:  xVel,
		yVel:  yVel,
	}
}

func (p probe) step() probe {
	newXVel := 0
	if p.xVel > 0 {
		newXVel = p.xVel - 1
	} else if p.xVel < 0 {
		newXVel = p.xVel + 1
	}

	return probe{
		start: p.start,
		pos:   newCoord(p.pos.x+p.xVel, p.pos.y+p.yVel),
		xVel:  newXVel,
		yVel:  p.yVel - 1,
	}
}

func (p probe) launch(target targetArea) bool {
	c := p
	for {
		c = c.step()
		if target.Contains(c) {
			return true
		} else if target.Missed(c) {
			return false
		}
	}
}

func (p probe) String() string {
	return fmt.Sprintf("Probe[pos: (%d, %d), xV: %d, yV: %d]", p.pos.x, p.pos.y, p.xVel, p.yVel)
}
