package d20

import (
	"fmt"
	"strings"
)

type ImageEnhancementAlgorithm []bool

func (iha ImageEnhancementAlgorithm) IsLight(idx int) bool {
	return iha[idx]
}

func (iha ImageEnhancementAlgorithm) IsAlternating() bool {
	return iha[0] == true && iha[511] == false
}

func ParseImageEnhancementAlgorithm(input string) (ImageEnhancementAlgorithm, error) {
	iha := make(ImageEnhancementAlgorithm, 512)
	for i, ch := range strings.TrimSpace(input) {
		if i > 511 {
			return nil, fmt.Errorf("out of bounds")
		}
		if ch == '#' {
			iha[i] = true
		} else if ch == '.' {
			iha[i] = false
		} else {
			return nil, fmt.Errorf("unexpected character: %c", ch)
		}
	}
	return iha, nil
}
