package engine

import (
	"context"
	"fmt"
	"log/slog"
	"peytob/isometricmmo/game/internal/engine/machine"
	"peytob/isometricmmo/game/public/fsm"
)

type Client interface {
	Run(ctx context.Context) error
}

type client struct {
	configuration *Configuration
	gameMachine   fsm.Machine[machine.GameMachineEvent]
	logger        *slog.Logger
}

func StartClient(_ context.Context, configuration *Configuration, logger *slog.Logger) (Client, error) {
	var err error

	initializingClient := &client{}
	initializingClient.configuration = configuration
	initializingClient.logger = logger

	initializingClient.gameMachine, err = initializeClientMachine()
	if err != nil {
		return nil, fmt.Errorf("error while initializing client machine: %w", err)
	}

	return initializingClient, nil
}

func (c *client) Run(ctx context.Context) error {
	return nil
}

func initializeClientMachine() (fsm.Machine[machine.GameMachineEvent], error) {
	return fsm.NewBuilder[machine.GameMachineEvent]().
		Build()
}
