package network

import (
	log "log/slog"
	"peytob/isometricmmo/game/internal/engine/core"
	"time"
)

type WebContext struct {
	appContext core.AppContext

	err *WebError
}

func NewWebContext(appContext core.AppContext) WebContext {
	return WebContext{
		appContext: appContext,
	}
}

func (w WebContext) Deadline() (deadline time.Time, ok bool) {
	return w.appContext.Deadline()
}

func (w WebContext) Done() <-chan struct{} {
	return w.appContext.Done()
}

func (w WebContext) Err() error {
	return w.err
}

func (w WebContext) Value(key any) any {
	return w.appContext.Value(key)
}

func (w WebContext) AppContext() core.AppContext {
	return w.appContext
}

func (w WebContext) Logger() *log.Logger {
	return w.appContext.App().Logger()
}
