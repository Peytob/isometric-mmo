package fsm

import (
	"context"
	"errors"
	"maps"
	"peytob/isometricmmo/game/public/utils"
)

type MachineBuilder[E comparable] interface {
	// RegisterState add State and all transitions to this Machine. If state already exists in
	// machine, then transitions will be merged.
	RegisterState(state State, transitions Transitions[E]) MachineBuilder[E]

	// RegisterStateFunc alias for RegisterState that uses functions instead of state struct
	RegisterStateFunc(f func(ctx context.Context) error, identifier StateIdentifier, transitions Transitions[E]) MachineBuilder[E]

	// WriteState registers state if state not exists. Rewrites transitions if state already exists.
	WriteState(state State, transitions Transitions[E]) MachineBuilder[E]

	// InitialState state that be used as initial on Machine. If initial state already exists it will be rewritten.
	InitialState(state StateIdentifier) MachineBuilder[E]

	// GlobalTransitions Add new global transactions to Machine. If global transitions already specified, then
	// new transitions will be rewritten
	GlobalTransitions(transitions Transitions[E]) MachineBuilder[E]

	// FinalStates states that be used as final on Machine. If not set, then machine will work indefinitely.
	FinalStates(states []StateIdentifier) MachineBuilder[E]

	// Build returns built machine. Possible error causes is:
	// State used in transition but not registered
	// If initial state not set
	// No one state is registered
	Build() (Machine[E], error)
}

type machineBuilder[E comparable] struct {
	transitions             map[StateIdentifier]Transitions[E]
	globalTransitions       Transitions[E]
	states                  map[StateIdentifier]State
	finalStates             utils.Set[StateIdentifier]
	initialState            StateIdentifier
	initialStateInitialized bool
}

// NewBuilder Creates new builder
func NewBuilder[E comparable]() MachineBuilder[E] {
	m := &machineBuilder[E]{
		transitions:             make(map[StateIdentifier]Transitions[E]),
		globalTransitions:       make(Transitions[E]),
		states:                  make(map[StateIdentifier]State),
		finalStates:             utils.NewSet[StateIdentifier](16),
		initialStateInitialized: false,
	}

	return m
}

func (m *machineBuilder[E]) RegisterState(state State, transitions Transitions[E]) MachineBuilder[E] {
	if existsTransitions, ok := m.transitions[state.Identifier()]; ok {
		maps.Copy(existsTransitions, transitions)
	} else {
		m.WriteState(state, transitions)
	}

	return m
}

func (m *machineBuilder[E]) RegisterStateFunc(f func(ctx context.Context) error, identifier StateIdentifier, transitions Transitions[E]) MachineBuilder[E] {
	return m.RegisterState(&funcState{
		identifier: identifier,
		update:     f,
	}, transitions)
}

func (m *machineBuilder[E]) WriteState(state State, transitions Transitions[E]) MachineBuilder[E] {
	m.transitions[state.Identifier()] = transitions
	m.states[state.Identifier()] = state
	return m
}

func (m *machineBuilder[E]) InitialState(state StateIdentifier) MachineBuilder[E] {
	m.initialState = state
	m.initialStateInitialized = true
	return m
}

func (m *machineBuilder[E]) GlobalTransitions(transitions Transitions[E]) MachineBuilder[E] {
	maps.Copy(m.globalTransitions, transitions)
	return m
}

func (m *machineBuilder[E]) FinalStates(states []StateIdentifier) MachineBuilder[E] {
	for _, state := range states {
		if !m.finalStates.Contains(state) {
			m.finalStates.Add(state)
		}
	}

	return m
}

func (m *machineBuilder[E]) Build() (Machine[E], error) {
	for leftState := range m.transitions {
		if !m.containsState(leftState) {
			return nil, errors.New(string("transition left finalState not found: " + leftState))
		}

		for event := range m.transitions[leftState] {
			rightState := m.transitions[leftState][event]

			if !m.containsState(rightState) {
				return nil, errors.New(string("transition right finalState not found: " + leftState))
			}
		}
	}

	for event := range m.globalTransitions {
		rightState := m.globalTransitions[event]

		if !m.containsState(rightState) {
			return nil, errors.New(string("transition right finalState not found: " + rightState))
		}
	}

	for finalState := range m.finalStates {
		if !m.containsState(finalState) {
			return nil, errors.New(string("final finalState not found: " + finalState))
		}
	}

	if !m.initialStateInitialized {
		return nil, errors.New("initial state not initialized")
	}

	if !m.containsState(m.initialState) {
		return nil, errors.New("initial state not registered")
	}

	return newMachine(m)
}

func (m *machineBuilder[E]) containsState(identifier StateIdentifier) bool {
	_, ok := m.states[identifier]
	return ok
}
