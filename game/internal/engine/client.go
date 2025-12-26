package engine

import "peytob/isometricmmo/game/internal/cfg"

type Client interface {
}

type client struct {
	configuration *cfg.ClientConfiguration
}

func StartClient(configuration *cfg.ClientConfiguration) Client {
	return &client{
		configuration: configuration,
	}
}
