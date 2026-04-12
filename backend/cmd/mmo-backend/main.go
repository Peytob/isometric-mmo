package main

import (
	"context"
	"os"
	"os/signal"
	"peytob/isometricmmo/backend/config"
	"peytob/isometricmmo/backend/internal/app"
	"syscall"
)

func main() {
	var err error

	cfg, err := config.LoadConfig()
	if err != nil {
		panic("failed to load configuration: " + err.Error())
	}

	rootCtx, cancel := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer cancel()

	application, err := app.NewApplication(rootCtx, cfg)
	if err != nil {
		panic("failed to initialize app: " + err.Error())
	}

	err = application.Run()
	if err != nil {
		panic("unhandled error during executing: " + err.Error())
	}
}
