package web

import (
	"context"
	"log/slog"
	"net"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
	"strconv"
)

type HttpServer interface {
	ListenAndServe() error
	Address() string
	Shutdown() error
}

type HttpServerOpts struct{}

type httpServer struct {
	server      http.Server
	root        http.Handler
	logger      *slog.Logger
	addr        string
	baseContext context.Context
}

func InitializeHttpServer(app core.App, root http.Handler) (HttpServer, error) {
	cfg := app.Configuration()
	addr := cfg.Http.Host + ":" + strconv.Itoa(cfg.Http.Port)

	return &httpServer{
		logger:      app.Logger().With("address", addr),
		addr:        addr,
		root:        root,
		baseContext: app.NewContext(),
	}, nil
}

func (s *httpServer) ListenAndServe() error {
	s.logger.Info("starting http server listening")

	s.server = http.Server{
		Addr:    s.addr,
		Handler: s.root,
		BaseContext: func(listener net.Listener) context.Context {
			return s.baseContext
		},
	}

	return s.server.ListenAndServe()
}

func (s *httpServer) Address() string {
	return s.addr
}

func (s *httpServer) Shutdown() error {
	s.logger.Info("shutting down http server")
	return s.server.Shutdown(s.baseContext)
}
