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

	logger.Info("Initializing server")

	initializingServer := &server{}
	initializingServer.configuration = configuration
	initializingServer.logger = logger
	initializingServer.storage = resource.NewServerStorage()

	initializingServer.httpServer, err = network.InitializeHttpServer(ctx, configuration, logger)
	if err != nil {
		return nil, fmt.Errorf("failed to initialize HTTP server: %w", err)
	}

	logger.Info("Server initialized")

	return initializingServer, nil
}

func (server *server) Http() network.HttpServer {
	return server.httpServer
}
