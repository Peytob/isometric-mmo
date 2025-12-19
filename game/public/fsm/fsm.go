package fsm

import (
	"context"
	"errors"
	"maps"
)

// Machine Abstract finite state machine.
type Machine[E comparable] interface {
	Update(ctx context.Context) error
	Event(event E) error
	Result() (state State, ok bool)
	IsRunning() bool
}

type StateIdentifier string

type State interface {
	Update(ctx context.Context) error
	Identifier() StateIdentifier
}

type Transitions[E comparable] map[E]StateIdentifier

type machine[E comparable] struct {
	transitions       map[StateIdentifier]Transitions[E]
	globalTransitions Transitions[E]

	states       map[StateIdentifier]State
	currentState State
	isRunning    bool
}

func newMachine[E comparable](builder *machineBuilder[E]) (Machine[E], error) {
	return machine[E]{
		transitions:       maps.Clone(builder.transitions),
		globalTransitions: maps.Clone(builder.globalTransitions),

		states:       builder.states,
		currentState: builder.initialState,
		isRunning:    true,
	}, nil
}

func (m machine[E]) Update(ctx context.Context) error {
	return m.currentState.Update(ctx)
}

func (m machine[E]) Event(event E) error {
	if nextStateIdentifier, ok := m.globalTransitions[event]; ok {
		return m.changeState(nextStateIdentifier)
	}

	if transitions, ok := m.transitions[m.currentState.Identifier()]; ok {
		if nextStateIdentifier, ok := transitions[event]; ok {
			return m.changeState(nextStateIdentifier)
		} else {
			return errors.New("unknown event")
		}
	} else {
		return errors.New("current state not found in transitions table")
	}
}

func (m machine[E]) Result() (state State, ok bool) {
	if !m.IsRunning() {
		return m.currentState, true
	} else {
		return nil, false
	}
}

func (m machine[E]) IsRunning() bool {
	return m.isRunning
}

func (m machine[E]) changeState(nextState StateIdentifier) error {
	if !m.isRunning {
		return errors.New("machine is not running")
	}

	if state, ok := m.states[nextState]; ok {
		m.currentState = state
		return nil
	} else {
		return errors.New("state not found")
	}
}
