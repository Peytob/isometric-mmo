package main

import (
	"context"
	"errors"
	"net/http"
	"os"
	"os/signal"
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/server"
	"syscall"

	"golang.org/x/sync/errgroup"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	cfg, err := core.LoadConfiguration("./config/server_config.yaml")
	if err != nil {
		panic(err)
	}

	logger, err := core.LoadServerLogger(ctx, cfg)
	if err != nil {
		panic(err)
	}

	app, err := server.InitializeServer(ctx, cfg, logger)
	if err != nil {
		panic(err)
	}

	errGroup, egContext := errgroup.WithContext(ctx)

	errGroup.Go(func() error {
		if err := app.Http().ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			return err
		}

		return nil
	})

	errGroup.Go(func() error {
		<-egContext.Done()
		return app.Http().Shutdown()
	})

	if err := errGroup.Wait(); err != nil {
		panic(err)
	}
}
