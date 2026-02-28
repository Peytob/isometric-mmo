package client

import (
	"context"
	"fmt"
	"log/slog"
	"peytob/isometricmmo/game/internal/client/graphic"
	"peytob/isometricmmo/game/internal/client/window"
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
	logger        *slog.Logger
	configuration *core.Configuration
	gameMachine   machine.Machine

	storage resource.Storage
	graphic graphic.Graphic
	window  window.Window
}

func StartClient(_ context.Context, configuration *core.Configuration, logger *slog.Logger) (Client, error) {
	var err error

	initializingClient := &client{}
	initializingClient.configuration = configuration
	initializingClient.logger = logger
	initializingClient.storage = resource.NewClientStorage()
	initializingClient.gameMachine = initializeClientMachine()

	if initializingClient.window, err = window.Init(); err != nil {
		return nil, fmt.Errorf("error while initializing graphic: %v", err)
	}

	if initializingClient.graphic, err = graphic.Init(initializingClient.window); err != nil {
		return nil, fmt.Errorf("error while initializing graphic: %v", err)
	}

	initializingClient.setUpWindowCallbacks()

	return initializingClient, nil
}

func (c *client) Run(ctx context.Context) error {
	for {
		var err error

		select {
		case <-ctx.Done():
			// Mark window as should close
			c.logger.Info("root context done, closing window")
			c.window.Close()
		default:
			// nothing, keep running
		}

		c.window.PoolEvents()

		if c.window.ShouldClose() {
			c.logger.Debug("stopping game machine")
			err = c.gameMachine.Event(machine.StoppedEvent)
			if err != nil {
				return fmt.Errorf("failed to stop machine on window close: %w", err)
			}
			break
		}

		event, err := c.gameMachine.State().Update(ctx)
		if err != nil {
			return fmt.Errorf("error while executing FSM machine update: %w", err)
		}

		if event != machine.NoEvent {
			err = c.gameMachine.Event(event)
			if err != nil {
				return fmt.Errorf("error while changing machine state: %w", err)
			}
		}

		if !c.gameMachine.IsRunning() {
			break
		}
	}

	return nil
}

func (c *client) Shutdown(_ context.Context) error {
	c.logger.Info("shutting down client")
	c.graphic.Terminate()
	c.window.Terminate()
	return nil
}

func (c *client) setUpWindowCallbacks() {
	c.window.OnClose(func(w window.Window) {
		w.Close()
	})
}

func initializeClientMachine() machine.Machine {
	return fsm.NewBuilder[machine.Event, machine.StateIdentifier, machine.State]().
		RegisterState(machine.NewInitialState(), make(fsm.Transitions[machine.Event, machine.StateIdentifier])).
		InitialState(machine.InitialStateIdentifier).
		RegisterState(machine.NewStoppedState(), make(fsm.Transitions[machine.Event, machine.StateIdentifier])).
		GlobalTransitions(fsm.Transitions[machine.Event, machine.StateIdentifier]{
			machine.StoppedEvent: machine.InitialStateIdentifier,
		}).
		ShouldBuild()
}
