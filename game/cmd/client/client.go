package main

import (
	"context"
	"peytob/isometricmmo/game/internal/engine"
	"runtime"
)

func main() {
	// Client main method should be executed in main application thread due
	// to C libraries restrictions
	runtime.LockOSThread()

	ctx := context.Background()
	cfg, err := engine.LoadClientConfiguration(ctx)
	if err != nil {
		panic(err)
	}

	logger, err := engine.LoadLogger(ctx, cfg)
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
