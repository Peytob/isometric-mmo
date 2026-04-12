package thttp

import (
	"context"
	"fmt"
	"log/slog"
	"net"
	"net/http"
	"peytob/isometricmmo/backend/config"
	"peytob/isometricmmo/backend/internal/service"
	"peytob/isometricmmo/backend/internal/transport/thttp/api"
	"strconv"
)

type Server struct {
	srv         http.Server
	root        http.Handler
	addr        string
	logger      *slog.Logger
	baseContext context.Context
}

type initializer interface {
	Config() config.Config
	Logger() *slog.Logger
	Service() service.Service
}

func Initialize(ctx context.Context, i initializer) (*Server, error) {
	port := i.Config().Http.Port
	a, err := api.NewApi(ctx, i.Logger(), i.Config(), i.Service())
	if err != nil {
		return nil, fmt.Errorf("error while initializing domain services: %w", err)
	}

	return &Server{
		root: a,
		// TODO Host restrictions configuration?
		addr:        ":" + strconv.Itoa(port),
		logger:      i.Logger(),
		baseContext: ctx,
	}, nil
}

func (s *Server) ListenAndServe() error {
	s.logger.Info("starting http server listening")

	s.srv = http.Server{
		Addr:    s.addr,
		Handler: s.root,
		BaseContext: func(listener net.Listener) context.Context {
			return s.baseContext
		},
	}

	return s.srv.ListenAndServe()
}

func (s *Server) Shutdown() error {
	s.logger.Info("shutting down http server")
	return s.srv.Shutdown(s.baseContext)
}
