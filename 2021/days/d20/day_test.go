package d20

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func getTestInput() (ImageEnhancementAlgorithm, Image) {
	input := "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
		"#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
		".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
		".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
		".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
		"...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
		"..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\n" +
		"\n" +
		"#..#.\n" +
		"#....\n" +
		"##..#\n" +
		"..#..\n" +
		"..###"
	iha, image, err := parseInput(input)
	if err != nil {
		panic(err)
	}
	return iha, image
}

func TestTotalLightPixelsAfterApplyingNTimes_2(t *testing.T) {
	iha, image := getTestInput()
	total := TotalLightPixelsAfterApplyingNTimes(iha, image, 2)
	assert.Equal(t, 35, total)
}

func TestTotalLightPixelsAfterApplyingNTimes_50(t *testing.T) {
	iha, image := getTestInput()
	total := TotalLightPixelsAfterApplyingNTimes(iha, image, 50)
	assert.Equal(t, 3351, total)
}
