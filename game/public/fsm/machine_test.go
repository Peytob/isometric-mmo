package fsm

import (
	"context"
	"testing"
)

const (
	initialStateIdentifier = StateIdentifier("initializing")
	runningStateIdentifier = StateIdentifier("running")
	loadingStateIdentifier = StateIdentifier("loading")
	exitedStateIdentifier  = StateIdentifier("exited")
	failedStateIdentifier  = StateIdentifier("failed")

	initializedEvent  = "initialized"
	failedEvent       = "failed"
	levelChangedEvent = "levelChanged"
	loadedEvent       = "loaded"
	exitedEvent       = "exited"
)

type initialState struct{}

func (s initialState) Update(_ context.Context) error {
	return nil
}

func (s initialState) Identifier() StateIdentifier {
	return initialStateIdentifier
}

func nothing(_ context.Context) error {
	return nil
}

func createTestMachine() Machine[string] {
	m, _ := NewBuilder[string]().
		RegisterState(initialState{}, Transitions[string]{
			initializedEvent: runningStateIdentifier,
		}).
		RegisterStateFunc(nothing, runningStateIdentifier, Transitions[string]{
			exitedEvent:       exitedStateIdentifier,
			levelChangedEvent: loadingStateIdentifier,
		}).
		RegisterStateFunc(nothing, loadingStateIdentifier, Transitions[string]{
			loadedEvent: runningStateIdentifier,
			failedEvent: initialStateIdentifier, // will be reset just for event priority testing
		}).
		RegisterStateFunc(nothing, failedStateIdentifier, Transitions[string]{}).
		RegisterStateFunc(nothing, exitedStateIdentifier, Transitions[string]{}).
		GlobalTransitions(Transitions[string]{
			failedEvent: failedStateIdentifier,
		}).
		InitialState(initialStateIdentifier).
		FinalStates([]StateIdentifier{failedStateIdentifier, exitedStateIdentifier}).
		Build()

	return m
}

func TestEventChanges(t *testing.T) {
	t.Run("should change machine state according to local transitions table", func(t *testing.T) {
		m := createTestMachine()

		if err := m.Event(initializedEvent); err != nil {
			t.Error(err)
		}

		if m.State().Identifier() != runningStateIdentifier {
			t.Errorf("wrong state after initialized event: %s", m.State().Identifier())
		}
	})

	t.Run("should change machine state according to global transitions table", func(t *testing.T) {
		m := createTestMachine()

		if err := m.Event(failedEvent); err != nil {
			t.Error(err)
		}

		if m.State().Identifier() != failedStateIdentifier {
			t.Errorf("wrong state after failed event: %s", m.State().Identifier())
		}
	})

	t.Run("should change machine with higher local transitions priority", func(t *testing.T) {
		m := createTestMachine()

		if err := m.Event(initializedEvent); err != nil {
			t.Error(err)
		}

		if err := m.Event(levelChangedEvent); err != nil {
			t.Error(err)
		}

		// Failed event on level changing state will throw machine back to initializing state
		if err := m.Event(failedEvent); err != nil {
			t.Error(err)
		}

		if m.State().Identifier() != initialStateIdentifier {
			t.Errorf("wrong state after failed event: %s", m.State().Identifier())
		}
	})
}

func TestFinalStates(t *testing.T) {
	t.Run("should not run after final state", func(t *testing.T) {
		m := createTestMachine()

		if err := m.Event(failedEvent); err != nil {
			t.Error(err)
		}

		if m.IsRunning() {
			t.Error("machine is running in final state")
		}
	})

	t.Run("should return result in final state", func(t *testing.T) {
		m := createTestMachine()

		if err := m.Event(failedEvent); err != nil {
			t.Error(err)
		}

		state, ok := m.Result()

		if !ok {
			t.Error("result flag is not ok in final state")
		}

		if state.Identifier() != failedStateIdentifier {
			t.Error("wrong state in machine result")
		}
	})

	t.Run("should run in not final state", func(t *testing.T) {
		m := createTestMachine()

		if !m.IsRunning() {
			t.Error("machine is not running in non final state")
		}
	})
}
