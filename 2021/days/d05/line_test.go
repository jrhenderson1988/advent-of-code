package d05

import (
	"fmt"
	"testing"
)

func TestLine_PointsOnLine(t *testing.T) {
	fmt.Println(NewLineFromCoords(0, 9, 5, 9).PointsOnLine())
	fmt.Println(NewLineFromCoords(2, 2, 4, 6).PointsOnLine())
	fmt.Println(NewLineFromCoords(5, 5, 8, 2).PointsOnLine())
	fmt.Println(NewLineFromCoords(8, 0, 0, 8).PointsOnLine())
}
