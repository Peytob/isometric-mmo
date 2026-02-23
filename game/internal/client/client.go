package client

import (
	"context"
	"fmt"
	"log/slog"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/machine"
	"peytob/isometricmmo/game/internal/engine/management/resource"
	"peytob/isometricmmo/game/public/fsm"
)

type Client interface {
	// TODO Add implementation for core.App

	Run(ctx context.Context) error
}

type client struct {
	configuration *core.Configuration
	gameMachine   machine.Machine
	logger        *slog.Logger
	storage       resource.Storage
}

func StartClient(_ context.Context, configuration *core.Configuration, logger *slog.Logger) (Client, error) {
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

	if event == machine.NoEvent {
		return nil
	}

	err = c.gameMachine.Event(event)
	if err != nil {
		return fmt.Errorf("error while changing machine state: %w", err)
	}

	return nil
}

func initializeClientMachine() (machine.Machine, error) {
	return fsm.NewBuilder[machine.Event, machine.StateIdentifier, machine.State]().
		RegisterState(machine.NewInitialState(), make(fsm.Transitions[machine.Event, machine.StateIdentifier])).
		InitialState(machine.InitialStateIdentifier).
		Build()
}
