package common

import "fmt"

func BinarySliceToUint64(slice []bool) uint64 {
	l := len(slice)
	if len(slice) > 64 {
		panic(fmt.Sprintf("too many bits %d", l))
	}

	var curr uint64 = 1
	var total uint64 = 0
	for i := l - 1; i >= 0; i-- {
		if slice[i] {
			total += curr
		}
		curr *= 2
	}
	return total
}
