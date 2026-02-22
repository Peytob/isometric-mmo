package api

import (
	"encoding/json"
	"github.com/go-chi/chi/v5"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/logic"
	"peytob/isometricmmo/game/internal/engine/network"
)

func initializeSystemApi(app core.App) chi.Router {
	systemRouter := chi.NewRouter()

	systemRouter.Method(http.MethodGet, "/ping", NewHandler(app, pingHandler))

	return systemRouter
}

func pingHandler(ctx network.WebContext, w http.ResponseWriter, r *http.Request) {
	res := logic.PingServer(ctx.AppContext())

	if err := json.NewEncoder(w).Encode(res); err != nil {
		ctx.Logger().Error("failed to write response")
		return
	}
}
