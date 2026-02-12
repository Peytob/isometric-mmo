package network

import (
	"github.com/go-chi/chi/v5"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
)

type Api struct {
	rootRouter *chi.Mux
}

func InitializeWebApi(app core.App) Api {
	root := chi.NewRouter()

	root.Get("/ping", func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("pong"))
	})

	return Api{
		rootRouter: root,
	}
}

func (a Api) RootRouter() http.Handler {
	return a.rootRouter
}
