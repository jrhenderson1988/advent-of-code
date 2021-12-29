package d22

import "aoc2021/common"

type cube struct {
	minX, maxX, minY, maxY, minZ, maxZ int
}

func newCube(minX, maxX, minY, maxY, minZ, maxZ int) cube {
	return cube{minX, maxX, minY, maxY, minZ, maxZ}
}

func (c cube) OverlapsWith(other cube) bool {
	if other.minX > c.maxX || other.maxX < c.minX {
		return false
	}

	if other.minY > c.maxY || other.maxY < c.minY {
		return false
	}

	if other.minZ > c.maxZ || other.maxZ < c.minZ {
		return false
	}

	return true
}

func (c cube) Area() int {
	return (c.maxX - c.minX + 1) * (c.maxY - c.minY + 1) * (c.maxZ - c.minZ + 1)
}

func (c cube) Split(other cube) []cube {
	// TODO - refactor and make clearer
	newCubes := make([]cube, 0)
	if !c.OverlapsWith(other) {
		newCubes = append(newCubes, c)
		return newCubes
	}

	if other.minX > c.minX {
		newCubes = append(newCubes, newCube(c.minX, other.minX-1, c.minY, c.maxY, c.minZ, c.maxZ))
	}
	if other.maxX < c.maxX {
		newCubes = append(newCubes, newCube(other.maxX+1, c.maxX, c.minY, c.maxY, c.minZ, c.maxZ))
	}
	if other.minY > c.minY {
		minX := max(c.minX, other.minX)
		maxX := min(c.maxX, other.maxX)
		newCubes = append(newCubes, newCube(minX, maxX, c.minY, other.minY-1, c.minZ, c.maxZ))
	}
	if other.maxY < c.maxY {
		minX := max(c.minX, other.minX)
		maxX := min(c.maxX, other.maxX)
		newCubes = append(newCubes, newCube(minX, maxX, other.maxY+1, c.maxY, c.minZ, c.maxZ))
	}
	if other.minZ > c.minZ {
		minX := max(c.minX, other.minX)
		maxX := min(c.maxX, other.maxX)
		minY := max(c.minY, other.minY)
		maxY := min(c.maxY, other.maxY)
		newCubes = append(newCubes, newCube(minX, maxX, minY, maxY, c.minZ, other.minZ-1))
	}
	if other.maxZ < c.maxZ {
		minX := max(c.minX, other.minX)
		maxX := min(c.maxX, other.maxX)
		minY := max(c.minY, other.minY)
		maxY := min(c.maxY, other.maxY)
		newCubes = append(newCubes, newCube(minX, maxX, minY, maxY, other.maxZ+1, c.maxZ))
	}
	return newCubes
}

func min(x, y int) int {
	return common.MinInt(x, y)
}

func max(x, y int) int {
	return common.MaxInt(x, y)
}
