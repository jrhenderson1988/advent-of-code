package main

import (
	"aoc2021/days"
	"aoc2021/days/d01"
	"aoc2021/days/d02"
	"aoc2021/days/d03"
	"aoc2021/days/d04"
	"aoc2021/days/d05"
	"aoc2021/days/d06"
	"aoc2021/days/d07"
	"aoc2021/days/d08"
	"aoc2021/days/d09"
	"aoc2021/days/d10"
	"aoc2021/days/d11"
	"aoc2021/days/d12"
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"strconv"
	"time"
)

func main() {
	if len(os.Args) < 2 {
		log.Fatalln("Expected day as argument")
	}

	day, err := strconv.Atoi(os.Args[1])
	if err != nil {
		log.Fatalln("Expected day to be an integer")
	}

	inputBytes, err := ioutil.ReadFile(fmt.Sprintf("inputs/%d.txt", day))
	if err != nil {
		log.Fatalf("Could not load input file: %s\n", err.Error())
	}

	fmt.Printf("=== Day %d ===\n", day)

	input := string(inputBytes)

	start := timeInMillis()
	result, err := execute(day, input)
	if err != nil {
		log.Fatalf("Error: %s\n", err.Error())
	}
	finish := timeInMillis()

	fmt.Println(result)
	fmt.Printf("Took: %dms", finish-start)
}

func execute(day int, input string) (days.Result, error) {
	switch day {
	case 1:
		return d01.Execute(input)
	case 2:
		return d02.Execute(input)
	case 3:
		return d03.Execute(input)
	case 4:
		return d04.Execute(input)
	case 5:
		return d05.Execute(input)
	case 6:
		return d06.Execute(input)
	case 7:
		return d07.Execute(input)
	case 8:
		return d08.Execute(input)
	case 9:
		return d09.Execute(input)
	case 10:
		return d10.Execute(input)
	case 11:
		return d11.Execute(input)
	case 12:
		return d12.Execute(input)
	default:
		return days.Result{}, errors.New(fmt.Sprintf("day %d is not yet implemented", day))
	}
}

func timeInMillis() int64 {
	return time.Now().UnixNano() / int64(time.Millisecond)
}
