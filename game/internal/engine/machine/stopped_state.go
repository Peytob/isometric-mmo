package machine

import (
	"context"
	"peytob/isometricmmo/game/internal/engine/core"
)

const StoppedStateIdentifier = StateIdentifier("stopped")

type StoppedState struct {
}

func NewStoppedState(_ core.App) State {
	return &StoppedState{}
}

func (s StoppedState) Update(_ context.Context) (Event, error) {
	return NoEvent, nil
}

func (s StoppedState) Identifier() StateIdentifier {
	return StoppedStateIdentifier
}
