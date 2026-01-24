package network

import (
	"context"
	"log/slog"
	"net/http"
	"peytob/isometricmmo/game/internal/engine/core"
	"strconv"
)

type HttpServer interface {
	ListenAndServe() error
	Address() string
	Shutdown(ctx context.Context) error
}

type HttpServerOpts struct{}

type httpServer struct {
	server http.Server
	logger *slog.Logger
}

func InitializeHttpServer(ctx context.Context, configuration *core.Configuration, logger *slog.Logger) (HttpServer, error) {
	addr := configuration.Http.Host + ":" + strconv.Itoa(configuration.Http.Port)

	return &httpServer{
		server: http.Server{
			Addr: addr,
			Handler: http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
				w.WriteHeader(http.StatusOK)
				w.Write([]byte("Hello World"))
			}),
		},
		logger: logger.With("address", addr),
	}, nil
}

func (server *httpServer) ListenAndServe() error {
	server.logger.Info("starting http server listening")
	return server.server.ListenAndServe()
}

func (server *httpServer) Address() string {
	return server.server.Addr
}

func (server *httpServer) Shutdown(ctx context.Context) error {
	server.logger.Info("shutting down http server")
	return server.server.Shutdown(ctx)
}
