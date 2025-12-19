package fsm

import (
	"errors"
	"maps"
	"slices"
)

type MachineBuilder[E comparable] interface {
	// RegisterState Add State and all transitions to this Machine. If state already exists in
	// machine, then transitions will be merged.
	RegisterState(state State, transitions Transitions[E]) MachineBuilder[E]

	// WriteState Registers state if state not exists. Rewrites transitions if state already exists.
	WriteState(state State, transitions Transitions[E]) MachineBuilder[E]

	// InitialState State that be used as initial on Machine. If initial state already exists it will be rewritten.
	InitialState(state State) MachineBuilder[E]

	// GlobalTransitions Add new global transactions to Machine. If global transitions already specified, then
	// new transitions will rewrire .
	GlobalTransitions(transitions Transitions[E]) MachineBuilder[E]

	// FinalStates States that be used as final on Machine. If not set, then machine will work indefinitely.
	FinalStates(states []State) MachineBuilder[E]

	// Build Returns built machine. Possible error causes is:
	// State used in transition but not registered
	// If initial state not set
	// No one state is registered
	Build() (Machine[E], error)
}

type machineBuilder[E comparable] struct {
	transitions       map[StateIdentifier]Transitions[E]
	globalTransitions Transitions[E]
	states            map[StateIdentifier]State
	finalStates       []StateIdentifier
	initialState      State
}

// NewBuilder Creates new builder with given initial state. State will be automatically registered without any
// transitions.
func NewBuilder[E comparable](initialState State) MachineBuilder[E] {
	m := &machineBuilder[E]{
		transitions:       make(map[StateIdentifier]Transitions[E]),
		globalTransitions: make(Transitions[E]),
		states:            make(map[StateIdentifier]State),
		finalStates:       make([]StateIdentifier, 0, 16),
		initialState:      initialState,
	}

	m.RegisterState(initialState, make(Transitions[E]))

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

func (m *machineBuilder[E]) WriteState(state State, transitions Transitions[E]) MachineBuilder[E] {
	m.transitions[state.Identifier()] = transitions
	m.states[state.Identifier()] = state
	return m
}

func (m *machineBuilder[E]) InitialState(state State) MachineBuilder[E] {
	m.initialState = state
	return m
}

func (m *machineBuilder[E]) GlobalTransitions(transitions Transitions[E]) MachineBuilder[E] {
	maps.Copy(m.globalTransitions, transitions)
	return m
}

func (m *machineBuilder[E]) FinalStates(states []State) MachineBuilder[E] {
	for _, state := range states {
		if !slices.Contains(m.finalStates, state.Identifier()) {
			m.finalStates = append(m.finalStates)
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

	for _, finalState := range m.finalStates {
		if !m.containsState(finalState) {
			return nil, errors.New(string("final finalState not found: " + finalState))
		}
	}

	return newMachine(m)
}

func (m *machineBuilder[E]) containsState(identifier StateIdentifier) bool {
	_, ok := m.states[identifier]
	return ok
}
