package d23

type State struct {
	burrow Burrow
	cost   int
}

func NewState(burrow Burrow, cost int) State {
	return State{burrow, cost}
}
