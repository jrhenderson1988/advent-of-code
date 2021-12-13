package common

import (
	"strings"
)

func NormalizeLineBreaks(input string) string {
	return strings.ReplaceAll(strings.ReplaceAll(input, "\r\n", "\n"), "\r", "\n")
}

func SplitLines(input string) []string {
	normalized := NormalizeLineBreaks(input)
	return strings.Split(normalized, "\n")
}
