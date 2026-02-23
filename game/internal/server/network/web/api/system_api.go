package api

import (
	"encoding/json"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/server/management/status"
	"peytob/isometricmmo/game/internal/server/network/web"

	"github.com/go-chi/chi/v5"
)

func initializeSystemApi(app core.App) chi.Router {
	systemRouter := chi.NewRouter()

	systemRouter.Method(http.MethodGet, "/ping", NewHandler(app, pingHandler))

	return systemRouter
}

func pingHandler(ctx web.Context, w http.ResponseWriter, r *http.Request) {
	res := status.GetServerStatus(ctx.AppContext())

	if err := json.NewEncoder(w).Encode(res); err != nil {
		ctx.Logger().Error("failed to write response")
		return
	}
}
