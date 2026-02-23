package machine

import (
	"context"
)

const ResourcesLoadingStateIdentifier = StateIdentifier("resources_loading")

type ResourcesLoadingState struct {
}

func NewResourcesLoadingState() State {
	return &ResourcesLoadingState{}
}

func (s ResourcesLoadingState) Update(_ context.Context) (Event, error) {
	return NoEvent, nil
}

func (s ResourcesLoadingState) Identifier() StateIdentifier {
	return ResourcesLoadingStateIdentifier
}
