package machine

import (
	"context"
	"peytob/isometricmmo/game/internal/engine/core"
)

const InitialStateIdentifier = StateIdentifier("initial")

type InitialState struct {
}

func NewInitialState(_ core.App) State {
	return &InitialState{}
}

func (s InitialState) Update(_ context.Context) (Event, error) {
	return NoEvent, nil
}

func (s InitialState) Identifier() StateIdentifier {
	return InitialStateIdentifier
}
