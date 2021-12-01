package common

import "strconv"

func StringToInt(input string) (int, error) {
	return strconv.Atoi(input)
}

func IntToString(input int) string {
	return strconv.Itoa(input)
}

func StringsToInts(input []string) ([]int, error) {
	ints := make([]int, len(input))
	for i, s := range input {
		value, err := StringToInt(s)
		if err != nil {
			return []int{}, err
		}
		ints[i] = value
	}
	return ints, nil
}
