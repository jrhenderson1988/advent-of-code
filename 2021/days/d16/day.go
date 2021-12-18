package d16

import (
	"aoc2021/days"
	"aoc2021/days/d16/bits"
	"strings"
)

func Execute(input string) (days.Result, error) {
	value, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	p1 := CalculateVersionSumOfAllPackets(value)
	p2 := EvaluatePackets(value)

	return days.NewIntResult(p1, p2), nil
}

func parseInput(input string) (string, error) {
	return strings.TrimSpace(input), nil
}

func CalculateVersionSumOfAllPackets(input string) int {
	message := bits.NewMessageFromHexadecimal(input)
	outerPacket := message.Packets()

	queue := make([]bits.Packet, 0)
	queue = append(queue, outerPacket)
	versionSum := 0
	for len(queue) > 0 {
		packet := queue[0]
		queue = queue[1:]

		switch p := packet.(type) {
		case bits.OperatorPacket:
			versionSum += p.GetVersion()
			queue = append(queue, p.GetChildren()...)
		case bits.LiteralPacket:
			versionSum += p.GetVersion()
		default:
			panic("unexpected value")
		}
	}

	return versionSum
}

func EvaluatePackets(input string) int {
	message := bits.NewMessageFromHexadecimal(input)
	outerPacket := message.Packets()
	return outerPacket.Evaluate()
}
