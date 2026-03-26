package machine

import (
	"context"
)

const StoppedStateIdentifier = StateIdentifier("stopped")

type StoppedState struct {
}

func NewStoppedState() State {
	return &StoppedState{}
}

func (s StoppedState) Update(_ context.Context) (Event, error) {
	return NoEvent, nil
}

func (s StoppedState) Identifier() StateIdentifier {
	return StoppedStateIdentifier
}
