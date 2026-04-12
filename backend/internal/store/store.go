package store

import (
	"log/slog"
	"peytob/isometricmmo/backend/config"
)

type Store struct {
	logger *slog.Logger
}

type initializer interface {
	Config() config.Config
	Logger() *slog.Logger
}

func Initialize(i initializer) (Store, error) {
	i.Logger().Debug("Initializing store")

	s := Store{}

	return s, nil
}
