package engine_machine

import (
	"context"
)

const InitialStateIdentifier = StateIdentifier("initial")

type InitialState struct {
}

func NewInitialState() State {
	return &InitialState{}
}

func (s InitialState) Update(_ context.Context) (Event, error) {
	return NoEvent, nil
}

func (s InitialState) Identifier() StateIdentifier {
	return InitialStateIdentifier
}
