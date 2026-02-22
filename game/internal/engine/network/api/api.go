package api

import (
	"github.com/go-chi/chi/v5"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
)

type Api struct {
	rootRouter chi.Router
}

func InitializeWebApi(app core.App) Api {
	root := chi.NewRouter().
		With() // TODO Middleware

	root.Mount("/system", initializeSystemApi(app))

	return Api{
		rootRouter: root,
	}
}

func (a Api) RootRouter() http.Handler {
	return a.rootRouter
}
