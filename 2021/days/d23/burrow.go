package d23

import (
	"aoc2021/common"
	"fmt"
	"strings"
)

const hallwaySize = 11
const roomAPosition = 2
const roomBPosition = 4
const roomCPosition = 6
const roomDPosition = 8

type Burrow struct {
	slots []Amphipod
}

func NewBurrow(hallway, roomA, roomB, roomC, roomD []Amphipod) Burrow {
	if len(hallway) != hallwaySize {
		panic(fmt.Sprintf("hallway must have size: %d", hallwaySize))
	}

	roomSize := len(roomA)
	if len(roomB) != roomSize || len(roomC) != roomSize || len(roomD) != roomSize {
		panic("all rooms must be the same size")
	}

	slots := make([]Amphipod, hallwaySize+(4*roomSize))
	for i, a := range hallway {
		slots[i] = a
		if i < roomSize {
			slots[hallwaySize+i] = roomA[i]
			slots[hallwaySize+roomSize+i] = roomB[i]
			slots[hallwaySize+(roomSize*2)+i] = roomC[i]
			slots[hallwaySize+(roomSize*3)+i] = roomD[i]
		}
	}

	return Burrow{slots}
}

func ParseBurrow(input string) (Burrow, error) {
	lines := common.SplitLines(strings.TrimSpace(input))
	if lines[0] != "#############" {
		return Burrow{}, fmt.Errorf("invalid input - first line is not #############")
	}
	hallway := make([]Amphipod, len(lines[0])-2)
	var err error
	for i, ch := range []rune(lines[1])[1 : len(lines[1])-1] {
		hallway[i], err = AmphipodFromRune(ch)
		if err != nil {
			return Burrow{}, err
		}
	}
	roomA := make([]Amphipod, 0)
	roomB := make([]Amphipod, 0)
	roomC := make([]Amphipod, 0)
	roomD := make([]Amphipod, 0)
	for _, line := range lines[2:] {
		line = line[1 : len(line)-1]
		var apA, apB, apC, apD Amphipod
		a := rune(line[roomAPosition])
		b := rune(line[roomBPosition])
		c := rune(line[roomCPosition])
		d := rune(line[roomDPosition])
		if a == '#' || b == '#' || c == '#' || d == '#' {
			break
		}

		apA, err = AmphipodFromRune(a)
		if err != nil {
			return Burrow{}, err
		}

		apB, err = AmphipodFromRune(b)
		if err != nil {
			return Burrow{}, err
		}

		apC, err = AmphipodFromRune(c)
		if err != nil {
			return Burrow{}, err
		}

		apD, err = AmphipodFromRune(d)
		if err != nil {
			return Burrow{}, err
		}

		roomA = append(roomA, apA)
		roomB = append(roomB, apB)
		roomC = append(roomC, apC)
		roomD = append(roomD, apD)
	}

	return NewBurrow(hallway, roomA, roomB, roomC, roomD), nil
}

func (b Burrow) NeighbouringStates() []State {
	neighbours := make([]State, 0)
	for i, amphipod := range b.slots {
		if amphipod == None {
			continue
		}

		if b.inHallway(i) {
			targetRoomHallwayIndex := b.roomToHallwayIndex(amphipod.TargetRoom())
			if b.roomSatisfied(amphipod.TargetRoom()) && b.isHallwayClearBetween(i, targetRoomHallwayIndex, true) {
				state := b.move(i, b.targetIndexInRoom(amphipod.TargetRoom()))
				neighbours = append(neighbours, state)
				continue
			}
		} else if b.inRoom(i) {
			if b.roomSatisfied(b.indexToRoom(i)) {
				continue
			}

			if !b.canLeaveRoom(i) {
				continue
			}

			targetRoomHallwayIndex := b.roomToHallwayIndex(amphipod.TargetRoom())
			currentRoom := b.indexToRoom(i)
			currentRoomHallwayIndex := b.roomToHallwayIndex(currentRoom)
			if b.roomSatisfied(amphipod.TargetRoom()) && b.isHallwayClearBetween(currentRoomHallwayIndex, targetRoomHallwayIndex, false) {
				state := b.move(i, b.targetIndexInRoom(amphipod.TargetRoom()))
				neighbours = append(neighbours, state)
				continue
			} else {
				for _, hallwayIndex := range b.openHallwayIndexes() {
					if b.isHallwayClearBetween(currentRoomHallwayIndex, hallwayIndex, false) {
						state := b.move(i, hallwayIndex)
						neighbours = append(neighbours, state)
					}
				}
				continue
			}
		}
	}
	return neighbours
}

func (b Burrow) ToString() string {
	sb := strings.Builder{}
	for _, amphipod := range b.slots {
		sb.WriteRune(amphipod.Label())
	}
	return sb.String()
}

func (b Burrow) IsTarget() bool {
	for i := 0; i < hallwaySize; i++ {
		if b.slots[i] != None {
			return false
		}
	}

	for i := hallwaySize; i < len(b.slots); i++ {
		if b.slots[i] == None {
			continue
		}

		if b.slots[i].TargetRoom() != b.indexToRoom(i) {
			return false
		}
	}

	return true
}

func (b Burrow) validateRoom(room int) {
	if room < 0 || room > 3 {
		panic(fmt.Sprintf("room %d is out of bounds", room))
	}
}

func (b Burrow) validateSlotIndex(slotIndex int) {
	if slotIndex < 0 || slotIndex >= len(b.slots) {
		panic(fmt.Sprintf("slot index %d is out of bounds", slotIndex))
	}
}

func (b Burrow) roomSize() int {
	return (len(b.slots) - hallwaySize) / 4
}

func (b Burrow) inHallway(slotIndex int) bool {
	b.validateSlotIndex(slotIndex)
	return slotIndex < hallwaySize
}

func (b Burrow) inRoom(slotIndex int) bool {
	b.validateSlotIndex(slotIndex)
	return slotIndex >= hallwaySize
}

func (b Burrow) indexToRoom(slotIndex int) int {
	b.validateSlotIndex(slotIndex)
	if b.inHallway(slotIndex) {
		return -1 // -1 if slot is in hallway
	}

	return (slotIndex - hallwaySize) / b.roomSize()
}

func (b Burrow) roomToIndex(room int) int {
	b.validateRoom(room)
	return hallwaySize + (b.roomSize() * room)
}

func (b Burrow) roomSatisfied(room int) bool {
	b.validateRoom(room)

	roomSize := b.roomSize()
	slotIndex := b.roomToIndex(room)
	for i := slotIndex; i < slotIndex+roomSize; i++ {
		if b.slots[i] != None && b.slots[i].TargetRoom() != room {
			return false
		}
	}
	return true
}

func (b Burrow) canLeaveRoom(slotIndex int) bool {
	b.validateSlotIndex(slotIndex)

	if b.slots[slotIndex] == None {
		panic("provided slot is 'none'")
	}

	room := b.indexToRoom(slotIndex)
	if room == -1 {
		return false
	}

	roomSlotIndex := b.roomToIndex(room)
	for i := roomSlotIndex; i < slotIndex; i++ {
		if b.slots[i] != None {
			return false
		}
	}

	return true
}

func (b Burrow) openHallwayIndexes() []int {
	openSlots := make([]int, 0)
	for i := 0; i < hallwaySize; i++ {
		if i == roomAPosition || i == roomBPosition || i == roomCPosition || i == roomDPosition {
			continue
		} else if b.slots[i] != None {
			continue
		}
		openSlots = append(openSlots, i)
	}
	return openSlots
}

func (b Burrow) isHallwayClearBetween(from, to int, ignoreFrom bool) bool {
	b.validateSlotIndex(from)
	b.validateSlotIndex(to)
	if !b.inHallway(from) || !b.inHallway(to) {
		panic("one or both slots are not in the hallway")
	}

	if ignoreFrom {
		if from < to {
			from += 1
		} else {
			from -= 1
		}
	}

	for i := common.MinInt(from, to); i <= common.MaxInt(from, to); i++ {
		if b.slots[i] != None {
			return false
		}
	}

	return true
}

func (b Burrow) roomToHallwayIndex(room int) int {
	b.validateRoom(room)
	switch room {
	case 0:
		return roomAPosition
	case 1:
		return roomBPosition
	case 2:
		return roomCPosition
	case 3:
		return roomDPosition
	default:
		panic("invalid room")
	}
}

func (b Burrow) targetIndexInRoom(room int) int {
	b.validateRoom(room)

	targetIndex := -1
	roomIndex := b.roomToIndex(room)
	for i := roomIndex; i < roomIndex+b.roomSize(); i++ {
		if b.slots[i] != None {
			break
		}
		targetIndex = i
	}

	if targetIndex == -1 {
		panic("room is not empty")
	}

	return targetIndex
}

func (b Burrow) distance(from, to int) int {
	dist := 0
	if b.inRoom(from) {
		room := b.indexToRoom(from)
		dist += (from - b.roomToIndex(room)) + 1
		from = b.roomToHallwayIndex(room)
	}
	if b.inRoom(to) {
		room := b.indexToRoom(to)
		dist += (to - b.roomToIndex(room)) + 1
		to = b.roomToHallwayIndex(room)
	}
	dist += common.MaxInt(from, to) - common.MinInt(from, to)

	return dist
}

func (b Burrow) move(from, to int) State {
	dist := b.distance(from, to)
	energyCost := dist * b.slots[from].EnergyCost()

	newSlots := make([]Amphipod, len(b.slots))
	copy(newSlots, b.slots)
	newSlots[from], newSlots[to] = newSlots[to], newSlots[from]

	newBurrow := Burrow{slots: newSlots}

	return NewState(newBurrow, energyCost)
}

func (b Burrow) Draw() string {
	sb := strings.Builder{}
	sb.WriteString("#")
	for i := 0; i < hallwaySize; i++ {
		sb.WriteRune('#')
	}
	sb.WriteString("#\n")

	sb.WriteString("#")
	for i := 0; i < hallwaySize; i++ {
		sb.WriteRune(b.slots[i].Label())
	}
	sb.WriteString("#\n")

	roomSize := b.roomSize()
	for row := 0; row <= roomSize; row++ {
		if row == 0 {
			sb.WriteRune('#')
		} else {
			sb.WriteRune(' ')
		}

		for i := 0; i < hallwaySize; i++ {
			if row == roomSize {
				if i >= roomAPosition-1 && i <= roomDPosition+1 {
					sb.WriteRune('#')
				} else {
					sb.WriteRune(' ')
				}
				continue
			}

			if i == roomAPosition {
				sb.WriteRune(b.slots[row+hallwaySize].Label())
			} else if i == roomBPosition {
				sb.WriteRune(b.slots[row+hallwaySize+roomSize].Label())
			} else if i == roomCPosition {
				sb.WriteRune(b.slots[row+hallwaySize+roomSize*2].Label())
			} else if i == roomDPosition {
				sb.WriteRune(b.slots[row+hallwaySize+roomSize*3].Label())
			} else {
				if row == 0 {
					sb.WriteRune('#')
				} else if i >= roomAPosition-1 && i <= roomDPosition+1 {
					sb.WriteRune('#')
				} else {
					sb.WriteRune(' ')
				}
			}
		}

		if row == 0 {
			sb.WriteRune('#')
		} else {
			sb.WriteRune(' ')
		}

		sb.WriteRune('\n')
	}

	return sb.String()
}

func (b Burrow) GetHallway() []Amphipod {
	hallway := make([]Amphipod, hallwaySize)
	copy(hallway, b.slots[:hallwaySize])
	return hallway
}

func (b Burrow) GetRoom(room int) []Amphipod {
	roomSize := b.roomSize()
	cpy := make([]Amphipod, roomSize)
	idx := b.roomToIndex(room)
	copy(cpy, b.slots[idx:idx+roomSize])
	return cpy
}
