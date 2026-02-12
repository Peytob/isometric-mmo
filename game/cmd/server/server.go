package main

import (
	"context"
	"errors"
	"net/http"
	"os"
	"os/signal"
	"peytob/isometricmmo/game/internal/engine"
	"peytob/isometricmmo/game/internal/engine/core"
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

	logger, err := engine.LoadServerLogger(ctx, cfg)
	if err != nil {
		panic(err)
	}

	server, err := engine.InitializeServer(ctx, cfg, logger)
	if err != nil {
		panic(err)
	}

	errGroup, egContext := errgroup.WithContext(ctx)

	errGroup.Go(func() error {
		if err := server.Http().ListenAndServe(ctx); err != nil && !errors.Is(err, http.ErrServerClosed) {
			return err
		}

		return nil
	})

	errGroup.Go(func() error {
		<-egContext.Done()
		return server.Http().Shutdown(ctx)
	})

	if err := errGroup.Wait(); err != nil {
		panic(err)
	}
}
