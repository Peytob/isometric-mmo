package main

import (
	"context"
	"os"
	"os/signal"
	"peytob/isometricmmo/game/internal/client"
	"peytob/isometricmmo/game/internal/engine/core"
	"runtime"
	"syscall"
)

func init() {
	// Client main method should be executed in main application thread due
	// to C libraries restrictions
	runtime.LockOSThread()
}

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	cfg, err := core.LoadConfiguration("./config/client_config.yaml")
	if err != nil {
		panic(err)
	}

	logger, err := core.LoadClientLogger(ctx, cfg)
	if err != nil {
		panic("failed to initialize logger:" + err.Error())
	}

	app, err := client.StartClient(ctx, cfg, logger)
	if err != nil {
		panic("failed to start client: " + err.Error())
	}

	err = app.Run(ctx)
	if err != nil {
		panic("unhandled error while running: " + err.Error())
	}
}
