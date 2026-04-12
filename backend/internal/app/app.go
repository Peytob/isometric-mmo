package app

import (
	"log/slog"
	"peytob/isometricmmo/backend/config"
)

type Application interface {
	Run() error
}

type application struct {
	logger *slog.Logger
}

func (a application) Run() error {
	return nil
}

func Initialize(_ config.Config) (Application, error) {
	return nil, nil
}
