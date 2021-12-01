package main

import (
	"aoc2021/days"
	"aoc2021/days/d01"
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"strconv"
)

func main() {
	if len(os.Args) < 2 {
		log.Fatalln("Expected day as argument")
	}

	day, err := strconv.Atoi(os.Args[1])
	if err != nil {
		log.Fatalln("Expected day to be an integer")
	}

	inputBytes, err  := ioutil.ReadFile(fmt.Sprintf("inputs/%d.txt", day))
	if err != nil {
		log.Fatalf("Could not load input file: %s\n", err.Error())
	}

	input := string(inputBytes)
	result, err := execute(day, input)
	if err != nil {
		log.Fatalf("Error: %s\n", err.Error())
	}

	fmt.Printf("Day %d:\n%s\n", day, result)
}

func execute(day int, input string) (days.Result, error) {
	switch day {
	case 1:
		return d01.Execute(input)
	default:
		return days.Result{}, errors.New(fmt.Sprintf("day %d is not yet implemented", day))
	}
}
