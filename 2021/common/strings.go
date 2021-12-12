package common

import (
	"strings"
)

func SplitLines(input string) []string {
	normalized := strings.ReplaceAll(strings.ReplaceAll(input, "\r\n", "\n"), "\r", "\n")
	return strings.Split(normalized, "\n")
}
