package machine

import (
	"context"
	"peytob/isometricmmo/game/public/fsm"
)

type StateIdentifier string

type State interface {
	fsm.State[StateIdentifier]

	Update(ctx context.Context) (Event, error)
}
