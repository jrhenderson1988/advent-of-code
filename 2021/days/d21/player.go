package d21

type Player struct {
	position int
	score    int
}

func NewPlayer(pos, score int) Player {
	return Player{pos, score}
}

func (p Player) MoveToPosition(newPosition int) Player {
	return NewPlayer(newPosition, p.score+newPosition+1)
}

func (p Player) Wins() bool {
	return p.score >= 1000
}