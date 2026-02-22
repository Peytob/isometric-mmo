package main

import (
	"context"
	"os"
	"os/signal"
	"peytob/isometricmmo/game/internal/engine"
	"peytob/isometricmmo/game/internal/engine/core"
	"runtime"
	"syscall"
)

func main() {
	// Client main method should be executed in main application thread due
	// to C libraries restrictions
	runtime.LockOSThread()

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	cfg, err := core.LoadConfiguration("./config/client_config.yaml")
	if err != nil {
		panic(err)
	}

	logger, err := engine.LoadClientLogger(ctx, cfg)
	if err != nil {
		panic(err)
	}

	client, err := engine.StartClient(ctx, cfg, logger)
	if err != nil {
		panic(err)
	}

	err = client.Run(ctx)
	if err != nil {
		panic(err)
	}
}
