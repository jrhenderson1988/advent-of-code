package d12

type path []string

func (p path) Contains(seg string) bool {
	for _, s := range p {
		if s == seg {
			return true
		}
	}
	return false
}

func (p path) Last() string {
	return p[len(p)-1]
}

func (p path) Complete() bool {
	return p.Last() == "end"
}

func (p path) Clone() path {
	newPath := make([]string, len(p))
	for i, seg := range p {
		newPath[i] = seg
	}
	return newPath
}

func (p path) HasVisitedASmallCaveTwice() bool {
	smallCavesSeen := make(map[string]bool)
	for _, seg := range p {
		if isSmallCave(seg) {
			_, seen := smallCavesSeen[seg]
			if seen {
				return true
			}
			smallCavesSeen[seg] = true
		}
	}
	return false
}
