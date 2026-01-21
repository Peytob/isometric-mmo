package engine_machine

import "peytob/isometricmmo/game/public/fsm"

type Machine fsm.Machine[Event, StateIdentifier, State]
