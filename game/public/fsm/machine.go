package fsm

import (
	"context"
	"fmt"
	"maps"
	"peytob/isometricmmo/game/public/utils"
)

type MachineError string

func (e MachineError) Error() string {
	return string(e)
}

const (
	// StateNotFoundError operation state not found inside machine
	StateNotFoundError = MachineError("state not found")

	// MachineNotRunningError operation not allowed for stopped machine
	MachineNotRunningError = MachineError("machine not running")

	// UnknownEventError event not found in transitions table
	UnknownEventError = MachineError("unknown event")
)

// Machine Abstract finite state machine.
type Machine[E comparable] interface {
	// Update synchronously calls current state update method
	Update(ctx context.Context) error

	// Event changes current machine state according to given event
	Event(event E) error

	// Result if machine on end state now - returns current state and true flag, nil and false otherwise
	Result() (state State, ok bool)

	// IsRunning is machine on end state now
	IsRunning() bool

	// State returns current machine state
	State() State
}

type StateIdentifier string

type State interface {
	Update(ctx context.Context) error
	Identifier() StateIdentifier
}

type funcState struct {
	identifier StateIdentifier
	update     func(ctx context.Context) error
}

func (f *funcState) Update(ctx context.Context) error {
	return f.update(ctx)
}

func (f *funcState) Identifier() StateIdentifier {
	return f.identifier
}

type Transitions[E comparable] map[E]StateIdentifier

type machine[E comparable] struct {
	transitions       map[StateIdentifier]Transitions[E]
	globalTransitions Transitions[E]

	states       map[StateIdentifier]State
	finalStates  utils.Set[StateIdentifier]
	currentState State
	isRunning    bool
}

func newMachine[E comparable](builder *machineBuilder[E]) (Machine[E], error) {
	initialState, initialStateFound := builder.states[builder.initialState]

	if !initialStateFound {
		return nil, fmt.Errorf("initial state not set: %w", StateNotFoundError)
	}

	return &machine[E]{
		transitions:       maps.Clone(builder.transitions),
		globalTransitions: maps.Clone(builder.globalTransitions),

		states:       builder.states,
		finalStates:  maps.Clone(builder.finalStates),
		currentState: initialState,
		isRunning:    true,
	}, nil
}

func (m *machine[E]) Update(ctx context.Context) error {
	return m.currentState.Update(ctx)
}

func (m *machine[E]) Event(event E) error {
	currentState := m.currentState.Identifier()

	if transitions, ok := m.transitions[currentState]; ok {
		if nextStateIdentifier, ok := transitions[event]; ok {
			return m.changeState(nextStateIdentifier)
		}
	} else {
		return fmt.Errorf("current state %s not found in transitions table: %w", currentState, StateNotFoundError)
	}

	if nextStateIdentifier, ok := m.globalTransitions[event]; ok {
		return m.changeState(nextStateIdentifier)
	}

	return UnknownEventError
}

func (m *machine[E]) Result() (state State, ok bool) {
	if !m.IsRunning() {
		return m.currentState, true
	} else {
		return nil, false
	}
}

func (m *machine[E]) IsRunning() bool {
	return m.isRunning
}

func (m *machine[E]) State() State {
	return m.currentState
}

func (m *machine[E]) changeState(nextState StateIdentifier) error {
	if !m.isRunning {
		return MachineNotRunningError
	}

	if state, ok := m.states[nextState]; ok {
		m.currentState = state
		m.isRunning = !m.finalStates.Contains(state.Identifier())
		return nil
	} else {
		return StateNotFoundError
	}
}
