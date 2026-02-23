package web

import (
	log "log/slog"
	"peytob/isometricmmo/game/internal/engine/core"
	"time"
)

type Context struct {
	appContext core.AppContext

	err *Error
}

func NewWebContext(appContext core.AppContext) Context {
	return Context{
		appContext: appContext,
	}
}

func (w Context) Deadline() (deadline time.Time, ok bool) {
	return w.appContext.Deadline()
}

func (w Context) Done() <-chan struct{} {
	return w.appContext.Done()
}

func (w Context) Err() error {
	return w.err
}

func (w Context) Value(key any) any {
	return w.appContext.Value(key)
}

func (w Context) AppContext() core.AppContext {
	return w.appContext
}

func (w Context) Logger() *log.Logger {
	return w.appContext.App().Logger()
}
