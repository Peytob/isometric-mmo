package core

import (
	log "log/slog"
	"peytob/isometricmmo/game/internal/resource"
)

type App interface {
	Storage() resource.Storage
	Logger() *log.Logger
}
