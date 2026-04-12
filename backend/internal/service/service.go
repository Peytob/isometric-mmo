package service

import (
	"log/slog"
	"peytob/isometricmmo/backend/config"
	"peytob/isometricmmo/backend/internal/store"
)

type Service struct {
}

type initializer interface {
	Config() config.Config
	Logger() *slog.Logger
	Store() store.Store
}

func Initialize(i initializer) (Service, error) {
	return Service{}, nil
}
