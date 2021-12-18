package bits

import (
	"fmt"
	"strings"
)

const (
	versionLength = 3
	typeIdLength  = 3
)

type Message struct {
	message []bool
}

func NewMessage(message []bool) *Message {
	return &Message{message}
}

func NewMessageFromHexadecimal(input string) *Message {
	binary := make([]bool, len(input)*4)
	for i, ch := range []rune(input) {
		ch = ch - 48
		if ch > 9 {
			ch -= 7
		}
		binary[i*4] = uint8(ch)|uint8(7) == 15
		binary[i*4+1] = uint8(ch)|uint8(11) == 15
		binary[i*4+2] = uint8(ch)|uint8(13) == 15
		binary[i*4+3] = uint8(ch)|uint8(14) == 15
	}

	return NewMessage(binary)
}

func (bm *Message) ToHexString() string {
	sb := strings.Builder{}
	for i := 0; i < len(bm.message); i += 4 {
		num, v := 0, 8
		for j := 0; j < 4; j++ {
			if bm.message[i+j] {
				num += v
			}
			v /= 2
		}
		if num < 10 {
			num += 48
		} else {
			num += 55
		}

		if i == 0 {
			sb.WriteString(fmt.Sprintf("  %c ", rune(num)))
		} else {
			sb.WriteString(fmt.Sprintf("| %c ", rune(num)))
		}
	}
	return sb.String()
}

func (bm *Message) ToBinaryString() string {
	sb := strings.Builder{}
	for _, bit := range bm.message {
		if bit {
			sb.WriteRune('1')
		} else {
			sb.WriteRune('0')
		}
	}
	return sb.String()
}

func (bm *Message) Packets() Packet {
	packets, _ := buildPackets(bm.message, 0, 1)
	if len(packets) != 1 {
		panic("there should only be one outer packet")
	}
	return packets[0]
}

func (bm *Message) String() string {
	sb := strings.Builder{}
	sb.WriteString("Message {\n")
	sb.WriteString("  -> hex: ")
	sb.WriteString(bm.ToHexString())
	sb.WriteRune('\n')
	sb.WriteString("  -> bin: ")
	sb.WriteString(bm.ToBinaryString())
	sb.WriteRune('\n')
	sb.WriteString("}\n")

	return sb.String()
}

func binaryToInt(binary []bool) int {
	if len(binary) == 0 {
		panic("binary slice cannot be empty")
	}

	mul := 1
	num := 0
	for i := len(binary) - 1; i >= 0; i-- {
		if binary[i] == true {
			num += mul
		}
		mul *= 2
	}
	return num
}

func buildPackets(message []bool, start, limit int) ([]Packet, int) {
	if limit < 0 {
		panic("limit must be 0 (for unlimited) or greater")
	}

	packets := make([]Packet, 0)
	totalPacketsRead := 0
	for {
		if limit > 0 && totalPacketsRead == limit {
			break
		} else if limit == 0 && isEndOfMessage(message, start) {
			break
		}

		var version, typeId int
		version, start = parseVersion(message, start)
		typeId, start = parseTypeId(message, start)

		var packet Packet
		if typeId == TypeIdLiteral {
			packet, start = buildLiteral(message, start, version)
			packets = append(packets, packet)
		} else if message[start] == true {
			packet, start = buildOperatorWithNumChildrenLengthTypeId(message, start, version, typeId)
			packets = append(packets, packet)
		} else {
			packet, start = buildOperatorWithBitsLengthTypeId(message, start, version, typeId)
			packets = append(packets, packet)
		}
		totalPacketsRead++
	}

	return packets, start
}

func parseVersion(message []bool, start int) (int, int) {
	version := binaryToInt(message[start : start+versionLength])
	start += versionLength
	return version, start
}

func parseTypeId(message []bool, start int) (int, int) {
	typeId := binaryToInt(message[start : start+typeIdLength])
	start += typeIdLength
	return typeId, start
}

func buildLiteral(message []bool, start int, version int) (LiteralPacket, int) {
	numberBits := make([]bool, 0)
	for message[start] == true {
		numberBits = append(numberBits, message[start+1:start+5]...)
		start += 5
	}
	numberBits = append(numberBits, message[start+1:start+5]...)
	packet := NewLiteralPacket(version, binaryToInt(numberBits))

	start += 5

	return packet, start
}

func buildOperatorWithBitsLengthTypeId(message []bool, start, version, typeId int) (OperatorPacket, int) {
	start += 1

	totalSubpacketBits := binaryToInt(message[start : start+15])
	start += 15

	subpacketBits := message[start : start+totalSubpacketBits]
	start += totalSubpacketBits

	children, _ := buildPackets(subpacketBits, 0, 0)
	packet := NewOperatorPacket(version, typeId, children)

	return packet, start
}

func buildOperatorWithNumChildrenLengthTypeId(message []bool, start, version, typeId int) (OperatorPacket, int) {
	start += 1

	totalSubpackets := binaryToInt(message[start : start+11])
	start += 11

	var children []Packet
	children, start = buildPackets(message, start, totalSubpackets)

	packet := NewOperatorPacket(version, typeId, children)
	return packet, start
}

func isEndOfMessage(message []bool, pos int) bool {
	for i := pos; i < len(message); i++ {
		if message[i] == true {
			return false
		}
	}
	return true
}
