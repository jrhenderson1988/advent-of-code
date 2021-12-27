package d21

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

func Execute(input string) (days.Result, error) {
	game, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	deterministic := NewDeterministicDie(100)
	winningScore := deterministic.Play(game)

	return days.NewIntResult(winningScore, -1), nil
}

func parseInput(input string) (Game, error) {
	startingPositionLines := common.SplitLines(strings.TrimSpace(input))
	if len(startingPositionLines) != 2 {
		return Game{}, fmt.Errorf("expected two starting position lines")
	}

	playerOnePos, playerTwoPos := -1, -1
	for _, line := range startingPositionLines {
		var err error
		if strings.HasPrefix(line, "Player 1 starting position: ") {
			playerOnePos, err = common.StringToInt(strings.TrimSpace(line[28:]))
			if err != nil {
				return Game{}, err
			}
		} else if strings.HasPrefix(line, "Player 2 starting position: ") {
			playerTwoPos, err = common.StringToInt(strings.TrimSpace(line[28:]))
			if err != nil {
				return Game{}, err
			}
		}
	}

	if playerOnePos == -1 || playerTwoPos == -1 {
		return Game{}, fmt.Errorf("missing starting position for one of the players")
	}

	return NewGame(playerOnePos, playerTwoPos), nil
}
