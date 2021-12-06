package d04

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"regexp"
	"strings"
)

func Execute(input string) (days.Result, error) {
	calls, boards, err := parseCallsAndBoards(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	finalScore, err := CalculateFinalScoreOfWinningBoard(calls, boards)
	if err != nil {
		return days.EmptyResult(), err
	}

	finalScoreOfLosingBoard, err := CalculateFinalScoreOfLastBoardToWin(calls, boards)
	if err != nil {
		return days.EmptyResult(), err
	}

	return days.NewIntResult(finalScore, finalScoreOfLosingBoard), nil
}

func CalculateFinalScoreOfWinningBoard(calls []int, boards []*Board) (int, error) {
	filtered, err := cloneBoards(boards)
	if err != nil {
		return 0, err
	}

	for _, call := range calls {
		for i := 0; i < len(filtered); i++ {
			filtered[i].Mark(call)
			if filtered[i].Wins() {
				return filtered[i].Score() * call, nil
			}
		}
	}

	return 0, fmt.Errorf("there are no winners")
}

func CalculateFinalScoreOfLastBoardToWin(calls []int, boards []*Board) (int, error) {
	filtered, err := cloneBoards(boards)
	if err != nil {
		return 0, err
	}

	for _, call := range calls {
		for i := 0; i < len(filtered); i++ {
			filtered[i].Mark(call)
			if filtered[i].Wins() {
				if len(filtered) == 1 {
					return filtered[i].Score() * call, nil
				}
				if i == (len(filtered) - 1) {
					filtered = filtered[:i]
				} else {
					filtered = append(filtered[:i], filtered[i+1:]...)
				}
				i--
			}
		}
	}
	return 0, fmt.Errorf("calls exhausted and there are still some boards that have not won")
}

func parseCallsAndBoards(input string) ([]int, []*Board, error) {
	input = fmt.Sprintf("%s\n", strings.TrimSpace(input))

	calls := make([]int, 0)
	boards := make([]*Board, 0)
	currBoard := make([][]int, 0)
	for _, line := range common.SplitLines(input) {
		line = strings.TrimSpace(line)

		if len(calls) == 0 {
			var err error
			calls, err = parseCalls(line)
			if err != nil {
				return nil, nil, err
			}
			continue
		}

		if line == "" {
			if len(currBoard) == 0 {
				continue
			}

			newBoard, err := NewBoard(currBoard)
			if err != nil {
				return nil, nil, err
			}
			boards = append(boards, newBoard)
			currBoard = make([][]int, 0)
			continue
		}

		row, err := parseRow(line)
		if err != nil {
			return nil, nil, err
		}
		currBoard = append(currBoard, row)
	}

	if len(currBoard) != 0 {
		newBoard, err := NewBoard(currBoard)
		if err != nil {
			return nil, nil, err
		}
		boards = append(boards, newBoard)
	}

	return calls, boards, nil
}

func parseCalls(line string) ([]int, error) {
	return common.StringsToInts(strings.Split(strings.TrimSpace(line), ","))
}

func parseRow(line string) ([]int, error) {
	re := regexp.MustCompile("\\s+")
	vals := re.Split(line, -1)
	ints, err := common.StringsToInts(vals)
	if err != nil {
		return nil, err
	}
	if len(ints) != 5 {
		return nil, fmt.Errorf("expected row of 5 numbers, %d returned", len(ints))
	}
	return ints, nil
}

func cloneBoards(boards []*Board) ([]*Board, error) {
	clones := make([]*Board, len(boards))
	for i, board := range boards {
		clone, err := board.Clone()
		if err != nil {
			return nil, err
		}
		clones[i] = clone
	}
	return clones, nil
}
