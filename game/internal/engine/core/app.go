package core

import (
	"context"
	log "log/slog"
	"peytob/isometricmmo/game/internal/engine/management/resource"
)

type App interface {
	Storage() resource.Storage
	Logger() *log.Logger
	Configuration() *Configuration

	NewContextFrom(parent context.Context) AppContext
	NewContext() AppContext
}

type AppContext struct {
	context.Context
	app App
}

func (ctx AppContext) App() App {
	return ctx.app
}

func NewAppContext(parent context.Context, app App) AppContext {
	return AppContext{
		Context: parent,
		app:     app,
	}
}
