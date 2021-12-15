package d14

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

func Execute(input string) (days.Result, error) {
	template, rules, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	diffAfter10Steps := DiffAfterSteps(template, rules, 10)
	diffAfter40Steps := DiffAfterSteps(template, rules, 40)

	return days.NewIntResult(diffAfter10Steps, diffAfter40Steps), nil
}

func DiffAfterSteps(template []rune, rules map[pair]rune, steps int) int {
	// add counts of each pair in the initial inputs
	counts := make(map[pair]int)
	for i := 0; i < len(template)-1; i++ {
		key := pair{first: template[i], second: template[i+1]}
		counts[key] += 1
	}

	// for each round/step: create a new "counts" by iterating through the existing pair keys and
	// creating two new entries in the new "counts" such that each pair XY, mapping to a rule
	// XY -> Z, results in two new pairs XZ and ZY (because we insert Z in between, which yields the
	// two new pairs). We increase the counts of those new pairs by the count value of the key we're
	// processing.
	for s := 0; s < steps; s++ {
		newCounts := make(map[pair]int)
		for p, count := range counts {
			insertionChar, exists := rules[p]
			if !exists {
				panic(fmt.Sprintf("no rule exists for pair: %c%c", p.first, p.second))
			}

			newCounts[newPair(p.first, insertionChar)] += count
			newCounts[newPair(insertionChar, p.second)] += count
		}
		counts = newCounts
	}

	// Count the number of times each letter appears by visiting each pair and taking the first item
	// (since we'll end up visiting and therefore counting all letters other than the last one) and
	// adding the counts of those together. We later add 1 to the count of the last letter in the
	// template, since that one is always constant.
	countsByLetter := make(map[rune]int)
	for p, count := range counts {
		countsByLetter[p.first] += count
	}
	countsByLetter[template[len(template)-1]] += 1

	// Finally, find the highest and lowest counts and subtract the latter from the former to yield
	// the result.
	highest, lowest := -1, -1
	for _, count := range countsByLetter {
		if highest == -1 || count > highest {
			highest = count
		}
		if lowest == -1 || count < lowest {
			lowest = count
		}
	}
	return highest - lowest
}

func parseInput(input string) ([]rune, map[pair]rune, error) {
	input = common.NormalizeLineBreaks(input)
	parts := strings.Split(input, "\n\n")
	if len(parts) != 2 {
		return nil, nil, fmt.Errorf("invalid input, expected 2 sections")
	}

	template := []rune(strings.TrimSpace(parts[0]))

	//insertionRules := make(map[rune]map[rune]rune, 0)
	insertionRules := make(map[pair]rune)
	for _, line := range common.SplitLines(strings.TrimSpace(parts[1])) {
		ruleParts := strings.Split(line, "->")
		if len(ruleParts) != 2 {
			return nil, nil, fmt.Errorf("invalid line, expected pair and insertion character")
		}

		characterPair := []rune(strings.TrimSpace(ruleParts[0]))
		if len(characterPair) != 2 {
			return nil, nil, fmt.Errorf("invalid line, expected 2 characters on left side")
		}

		insertionCharacter := []rune(strings.TrimSpace(ruleParts[1]))
		if len(insertionCharacter) != 1 {
			return nil, nil, fmt.Errorf("invalid line, expected 1 character on right side")
		}

		key := pair{first: characterPair[0], second: characterPair[1]}
		insertionRules[key] = insertionCharacter[0]
	}

	return template, insertionRules, nil
}
