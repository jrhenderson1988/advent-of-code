package d14

type pair struct {
	first  rune
	second rune
}

func newPair(first, second rune) pair {
	return pair{first, second}
}
