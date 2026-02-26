package client

import (
	"context"
	"fmt"
	"log/slog"
	"peytob/isometricmmo/game/internal/client/graphic"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/machine"
	"peytob/isometricmmo/game/internal/engine/management/resource"
	"peytob/isometricmmo/game/public/fsm"
)

type Client interface {
	// TODO Add implementation for core.App

	Run(ctx context.Context) error
	Shutdown(ctx context.Context) error
}

type client struct {
	configuration *core.Configuration
	gameMachine   machine.Machine
	logger        *slog.Logger
	storage       resource.Storage
	graphic       graphic.Graphic
}

func StartClient(_ context.Context, configuration *core.Configuration, logger *slog.Logger) (Client, error) {
	var err error

	initializingClient := &client{}
	initializingClient.configuration = configuration
	initializingClient.logger = logger
	initializingClient.storage = resource.NewClientStorage()
	initializingClient.gameMachine = initializeClientMachine()

	if initializingClient.graphic, err = graphic.InitGraphic(); err != nil {
		return nil, fmt.Errorf("error while initializing graphic: %v", err)
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

func (c *client) Shutdown(_ context.Context) error {
	c.logger.Info("shutting down client")
	c.graphic.Terminate()
	return nil
}

func initializeClientMachine() machine.Machine {
	return fsm.NewBuilder[machine.Event, machine.StateIdentifier, machine.State]().
		RegisterState(machine.NewInitialState(), make(fsm.Transitions[machine.Event, machine.StateIdentifier])).
		InitialState(machine.InitialStateIdentifier).
		ShouldBuild()
}
