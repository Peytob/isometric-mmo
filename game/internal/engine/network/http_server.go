package network

import (
	"context"
	"log/slog"
	"net"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
	"strconv"
)

type HttpServer interface {
	ListenAndServe(ctx context.Context) error
	Address() string
	Shutdown(ctx context.Context) error
}

type HttpServerOpts struct{}

type httpServer struct {
	server http.Server
	root   http.Handler
	logger *slog.Logger
	addr   string
}

func InitializeHttpServer(configuration *core.Configuration, logger *slog.Logger, root http.Handler) (HttpServer, error) {
	addr := configuration.Http.Host + ":" + strconv.Itoa(configuration.Http.Port)

	return &httpServer{
		logger: logger.With("address", addr),
		addr:   addr,
		root:   root,
	}, nil
}

func (s *httpServer) ListenAndServe(ctx context.Context) error {
	s.logger.Info("starting http server listening")

	s.server = http.Server{
		Addr:    s.addr,
		Handler: s.root,
		BaseContext: func(listener net.Listener) context.Context {
			return ctx
		},
	}

	return s.server.ListenAndServe()
}

func (s *httpServer) Address() string {
	return s.addr
}

func (s *httpServer) Shutdown(ctx context.Context) error {
	s.logger.Info("shutting down http s")
	return s.server.Shutdown(ctx)
}
