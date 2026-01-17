package engine

import (
	"context"
	"fmt"
	"log/slog"
	"peytob/isometricmmo/game/internal/engine/engine_machine"
	"peytob/isometricmmo/game/internal/resource"
	"peytob/isometricmmo/game/public/fsm"
)

type Client interface {
	Run(ctx context.Context) error
}

type client struct {
	configuration *Configuration
	gameMachine   engine_machine.Machine
	logger        *slog.Logger
	storage       resource.Storage
}

func StartClient(_ context.Context, configuration *Configuration, logger *slog.Logger) (Client, error) {
	var err error

	initializingClient := &client{}
	initializingClient.configuration = configuration
	initializingClient.logger = logger
	initializingClient.storage = resource.NewClientStorage()

	initializingClient.gameMachine, err = initializeClientMachine()
	if err != nil {
		return nil, fmt.Errorf("error while initializing client machine: %w", err)
	}

	return initializingClient, nil
}

func (c *client) Run(ctx context.Context) error {
	event, err := c.gameMachine.State().Update(ctx)
	if err != nil {
		return fmt.Errorf("error while executing FSM machine update: %w", err)
	}

	if event == engine_machine.NoEvent {
		return nil
	}

	err = c.gameMachine.Event(event)
	if err != nil {
		return fmt.Errorf("error while changing machine state: %w", err)
	}

	return nil
}

func initializeClientMachine() (engine_machine.Machine, error) {
	return fsm.NewBuilder[engine_machine.Event, engine_machine.StateIdentifier, engine_machine.State]().
		RegisterState(engine_machine.NewInitialState(), make(fsm.Transitions[engine_machine.Event, engine_machine.StateIdentifier])).
		InitialState(engine_machine.InitialStateIdentifier).
		Build()
}
