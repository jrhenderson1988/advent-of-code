package d22

import (
	"aoc2021/common"
	"aoc2021/days"
	"strings"
)

func Execute(input string) (days.Result, error) {
	instructions, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalOnCubesWithinInitializationRegion := TotalOnCubesWithinInitializationRegion(instructions)
	totalOnCubes := TotalOnCubes(instructions)

	return days.NewIntResult(totalOnCubesWithinInitializationRegion, totalOnCubes), nil
}

func parseInput(input string) ([]Instruction, error) {
	instructions := make([]Instruction, 0)
	for _, line := range common.SplitLines(strings.TrimSpace(input)) {
		instruction, err := ParseInstruction(line)
		if err != nil {
			return nil, err
		}
		instructions = append(instructions, instruction)
	}
	return instructions, nil
}

func TotalOnCubesWithinInitializationRegion(instructions []Instruction) int {
	onCubes := make(map[coord]struct{})
	for _, instruction := range instructions {
		for x := instruction.x.from; x <= instruction.x.to; x++ {
			if x < -50 || x > 50 {
				continue
			}

			for y := instruction.y.from; y <= instruction.y.to; y++ {
				if y < -50 || y > 50 {
					continue
				}

				for z := instruction.z.from; z <= instruction.z.to; z++ {
					if z < -50 || z > 50 {
						continue
					}

					if instruction.on {
						onCubes[newCoord(x, y, z)] = struct{}{}
					} else {
						delete(onCubes, newCoord(x, y, z))
					}
				}
			}
		}
	}

	return len(onCubes)
}

func TotalOnCubes(instructions []Instruction) int {
	cubes := make([]cube, 0)
	for _, instr := range instructions {
		n := newCube(instr.x.from, instr.x.to, instr.y.from, instr.y.to, instr.z.from, instr.z.to)
		newCubes := make([]cube, 0)
		for i := 0; i < len(cubes); i++ {
			newCubes = append(newCubes, cubes[i].Split(n)...)
		}
		if instr.on {
			newCubes = append(newCubes, newCube(n.minX, n.maxX, n.minY, n.maxY, n.minZ, n.maxZ))
		}
		cubes = newCubes
	}

	count := 0
	for _, c := range cubes {
		count += c.Area()
	}
	return count
}
