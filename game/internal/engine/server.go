package engine

import (
	"context"
	"fmt"
	"log/slog"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/network"
	"peytob/isometricmmo/game/internal/resource"
)

type Server interface {
	Http() network.HttpServer
}

type server struct {
	configuration *core.Configuration
	logger        *slog.Logger
	storage       resource.Storage

	httpServer network.HttpServer
}

func InitializeServer(ctx context.Context, configuration *core.Configuration, logger *slog.Logger) (Server, error) {
	var err error

	logger.Info("initializing server")

	initializingServer := &server{}
	initializingServer.configuration = configuration
	initializingServer.logger = logger
	initializingServer.storage = resource.NewServerStorage()

	api := network.InitializeWebApi(initializingServer)
	initializingServer.httpServer, err = network.InitializeHttpServer(configuration, logger, api.RootRouter())
	if err != nil {
		return nil, fmt.Errorf("failed to initialize HTTP server: %w", err)
	}

	logger.Info("server initialized")

	return initializingServer, nil
}

func (server *server) Http() network.HttpServer {
	return server.httpServer
}

func (server *server) Storage() resource.Storage {
	return server.storage
}

func (server *server) Logger() *slog.Logger {
	return server.logger
}
