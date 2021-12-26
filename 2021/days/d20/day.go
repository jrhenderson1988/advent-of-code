package d20

import (
	"aoc2021/common"
	"aoc2021/days"
	"fmt"
	"strings"
)

func Execute(input string) (days.Result, error) {
	iha, image, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	totalAfterApplyingTwice := TotalLightPixelsAfterApplyingNTimes(iha, image, 2)
	totalAfterApplying50Times := TotalLightPixelsAfterApplyingNTimes(iha, image, 50)

	return days.NewIntResult(totalAfterApplyingTwice, totalAfterApplying50Times), nil
}

func parseInput(input string) (ImageEnhancementAlgorithm, Image, error) {
	segments := strings.Split(common.NormalizeLineBreaks(strings.TrimSpace(input)), "\n\n")
	if len(segments) != 2 {
		return ImageEnhancementAlgorithm{}, Image{}, fmt.Errorf("invalid input - expected two segments")
	}
	iha, err := ParseImageEnhancementAlgorithm(segments[0])
	if err != nil {
		return ImageEnhancementAlgorithm{}, Image{}, err
	}
	image, err := ParseImage(segments[1])
	if err != nil {
		return ImageEnhancementAlgorithm{}, Image{}, err
	}
	return iha, image, nil
}

func TotalLightPixelsAfterApplyingNTimes(iha ImageEnhancementAlgorithm, image Image, times int) int {
	if times < 1 {
		panic("expected times to be > 1")
	}

	img := image
	for i := 0; i < times; i++ {
		img = img.Apply(iha)
	}

	return img.TotalLightPixels()
}
