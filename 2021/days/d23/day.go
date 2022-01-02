package d23

import (
	"aoc2021/days"
	"math"
)

func Execute(input string) (days.Result, error) {
	burrow, err := parseInput(input)
	if err != nil {
		return days.EmptyResult(), err
	}

	minEnergy := MinimumEnergyToOrganiseAmphipods(burrow)
	minEnergyUnfolded := MinimumEnergyToOrganiseAmphipods(unfoldBurrow(burrow))

	return days.NewIntResult(minEnergy, minEnergyUnfolded), nil
}

func unfoldBurrow(burrow Burrow) Burrow {
	hallway := burrow.GetHallway()
	roomA := burrow.GetRoom(0)
	roomA = append(append(roomA[0:1], D, D), roomA[1:]...)
	roomB := burrow.GetRoom(1)
	roomB = append(append(roomB[0:1], C, B), roomB[1:]...)
	roomC := burrow.GetRoom(2)
	roomC = append(append(roomC[0:1], B, A), roomC[1:]...)
	roomD := burrow.GetRoom(3)
	roomD = append(append(roomD[0:1], A, C), roomD[1:]...)

	return NewBurrow(hallway, roomA, roomB, roomC, roomD)
}

func MinimumEnergyToOrganiseAmphipods(source Burrow) int {
	dist := make(map[string]int)
	prev := make(map[string]string)

	dist[source.ToString()] = 0

	q := NewMinPriorityQueue()
	q.Add(source, 0)

	for q.Len() > 0 {
		u := q.Pop()
		uSer := u.ToString()

		if u.IsTarget() {
			cost, costExists := dist[uSer]
			if !costExists {
				panic("cost does not exist")
			}
			return cost
		}

		for _, neighbour := range u.NeighbouringStates() {
			v := neighbour.burrow
			vSer := v.ToString()
			distU, distUOk := dist[uSer]
			if !distUOk {
				distU = math.MaxInt32
			}
			alt := distU + neighbour.cost
			distV, distVOk := dist[vSer]
			if !distVOk {
				distV = math.MaxInt32
			}
			if alt < distV {
				dist[vSer] = alt
				prev[uSer] = vSer
				q.Add(v, alt)
			}
		}

	}

	return -1
}

func parseInput(input string) (Burrow, error) {
	return ParseBurrow(input)
}
