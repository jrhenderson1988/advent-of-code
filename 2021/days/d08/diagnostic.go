package d08

import (
	"fmt"
	"strings"
)

type Diagnostic struct {
	digits [10]Signal
	output [4]Signal
}

func NewDiagnostic(digitSignals []Signal, outputSignals []Signal) (Diagnostic, error) {
	if len(digitSignals) != 10 {
		return Diagnostic{}, fmt.Errorf("expected exactly 10 digits, received %d", len(digitSignals))
	}
	var digits [10]Signal
	copy(digits[:], digitSignals)

	if len(outputSignals) != 4 {
		return Diagnostic{}, fmt.Errorf("expected exactly 4 digits")
	}
	var output [4]Signal
	copy(output[:], outputSignals)

	return Diagnostic{digits, output}, nil
}

func ParseDiagnostic(line string) (Diagnostic, error) {
	line = strings.TrimSpace(line)
	outerParts := strings.Split(line, "|")
	if len(outerParts) != 2 {
		return Diagnostic{}, fmt.Errorf("expected a single pipe character")
	}

	digitSignals := strings.Split(strings.TrimSpace(outerParts[0]), " ")
	digits := make([]Signal, len(digitSignals))
	for i, digitSignal := range digitSignals {
		digit, err := ParseSignal(strings.TrimSpace(digitSignal))
		if err != nil {
			return Diagnostic{}, err
		}
		digits[i] = digit
	}

	outputSignals := strings.Split(strings.TrimSpace(outerParts[1]), " ")
	outputs := make([]Signal, len(outputSignals))
	for i, outputSignal := range outputSignals {
		output, err := ParseSignal(strings.TrimSpace(outputSignal))
		if err != nil {
			return Diagnostic{}, err
		}
		outputs[i] = output
	}

	return NewDiagnostic(digits, outputs)
}

func (d Diagnostic) DigitSignals() []Signal {
	return d.digits[:]
}

func (d Diagnostic) OutputSignals() []Signal {
	return d.output[:]
}

func (d Diagnostic) String() string {
	sb := strings.Builder{}

	sb.WriteRune('\n')

	for i, sig := range d.digits {
		if i != 0 {
			sb.WriteString(" ")
		}
		sb.WriteString(sig.String())
	}

	sb.WriteString(" | ")

	for i, out := range d.output {
		if i != 0 {
			sb.WriteString(" ")
		}
		sb.WriteString(out.String())
	}

	return sb.String()
}

func (d Diagnostic) OutputAsNumber() int {
	nums := make(map[int]Signal)

	// Known digits
	for _, digit := range d.digits {
		l := digit.Len()
		if l == 2 {
			nums[1] = digit
		} else if l == 4 {
			nums[4] = digit
		} else if l == 3 {
			nums[7] = digit
		} else if l == 7 {
			nums[8] = digit
		}
	}
	if len(nums) != 4 {
		panic("expected to derive exactly 4 known numbers")
	}

	// derive the rest
	lShape := nums[4].Diff(nums[1])
	for _, digit := range d.digits {
		l := digit.Len()
		if l == 5 {
			if digit.Contains(lShape) {
				nums[5] = digit
			} else if digit.Contains(nums[1]) {
				nums[3] = digit
			} else {
				nums[2] = digit
			}
		} else if l == 6 {
			if !digit.Contains(lShape) {
				nums[0] = digit
			} else if digit.Contains(nums[1]) {
				nums[9] = digit
			} else {
				nums[6] = digit
			}
		}
	}
	if len(nums) != 10 {
		panic("could not work out all of the numbers")
	}

	// derive the output number using the nums map
	outputNumber := 0
	mul := 1000
	for _, outputSignal := range d.OutputSignals() {
		for n, sig := range nums {
			if sig.Equals(outputSignal) {
				outputNumber += n * mul
				mul = mul / 10
			}
		}
	}

	return outputNumber
}


//   2:      3:
//  aaaa    aaaa
// .    c  .    c
// .    c  .    c
//  dddd    dddd
// e    .  .    f
// e    .  .    f
//  gggg    gggg
//
//   5:
//  aaaa
// b    .
// b    .
//  dddd
// .    f
// .    f
//  gggg