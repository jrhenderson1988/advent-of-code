package d22

type coord struct {
	x int
	y int
	z int
}

func newCoord(x, y, z int) coord {
	return coord{x, y, z}
}
