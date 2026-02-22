package engine

import (
	"context"
	"fmt"
	log "log/slog"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/network"
	"peytob/isometricmmo/game/internal/engine/network/api"
	"peytob/isometricmmo/game/internal/resource"
)

type Server interface {
	Http() network.HttpServer
}

type server struct {
	rootContext context.Context

	configuration *core.Configuration
	logger        *log.Logger
	storage       resource.Storage

	httpServer network.HttpServer
}

func InitializeServer(ctx context.Context, configuration *core.Configuration, logger *log.Logger) (Server, error) {
	var err error

	logger.Info("initializing server")

	initializingServer := &server{}

	initializingServer.rootContext = ctx

	initializingServer.configuration = configuration
	initializingServer.logger = logger
	initializingServer.storage = resource.NewServerStorage()

	webApi := api.InitializeWebApi(initializingServer)
	initializingServer.httpServer, err = network.InitializeHttpServer(initializingServer, webApi.RootRouter())
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

func (server *server) Configuration() *core.Configuration {
	return server.configuration
}

func (server *server) Logger() *log.Logger {
	return server.logger
}

func (server *server) NewContextFrom(parent context.Context) core.AppContext {
	if appContext, ok := parent.(core.AppContext); ok {
		return appContext
	}

	return core.NewAppContext(parent, server)
}

func (server *server) NewContext() core.AppContext {
	return server.NewContextFrom(server.rootContext)
}
