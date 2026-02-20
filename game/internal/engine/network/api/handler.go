package api

import (
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/network"
)

type handlerFunc func(appContext network.WebContext, w http.ResponseWriter, r *http.Request)

type Handler struct {
	app     core.App
	handler handlerFunc
}

func (h Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	ctx := h.app.NewContextFrom(r.Context())
	h.handler(network.NewWebContext(ctx), w, r)
}

func NewHandler(app core.App, handler handlerFunc) Handler {
	return Handler{app: app, handler: handler}
}
