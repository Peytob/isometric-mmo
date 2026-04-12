package app

import (
	"context"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"peytob/isometricmmo/backend/config"
	"peytob/isometricmmo/backend/internal/service"
	"peytob/isometricmmo/backend/internal/store"
	"peytob/isometricmmo/backend/internal/transport/thttp"

	"golang.org/x/sync/errgroup"
)

type App interface {
	Run() error

	Config() config.Config
	Logger() *slog.Logger

	Store() store.Store
	Service() service.Service
}

type application struct {
	logger  *slog.Logger
	cfg     config.Config
	rootCtx context.Context

	store   store.Store
	service service.Service
	srv     *thttp.Server
}

func NewApplication(ctx context.Context, cfg config.Config) (App, error) {
	var err error

	logger := slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
		Level: cfg.Log.Level,
	}))

	logger.Info("starting app initialization")

	app := application{
		logger:  logger,
		cfg:     cfg,
		rootCtx: ctx,
	}

	app.store, err = store.Initialize(app)
	if err != nil {
		return nil, fmt.Errorf("error while initializing store: %w", err)
	}

	app.service, err = service.Initialize(app)
	if err != nil {
		return nil, fmt.Errorf("error while initializing domain services: %w", err)
	}

	app.srv, err = thttp.Initialize(app.rootCtx, app)
	if err != nil {
		return nil, fmt.Errorf("error while initializing thttp server: %w", err)
	}

	return app, nil
}

func (a application) Run() error {
	eg, egContext := errgroup.WithContext(a.rootCtx)

	eg.Go(func() error {
		if err := a.srv.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			return err
		}

		return nil
	})

	eg.Go(func() error {
		<-a.rootCtx.Done()
		a.logger.Info("starting gracefully shutdown")
		return nil
	})

	eg.Go(func() error {
		<-egContext.Done()
		return a.shutdown()
	})

	if err := eg.Wait(); err != nil {
		// TODO Try to restore on error?
		return fmt.Errorf("error during app running: %w", err)
	}

	return nil
}

func (a application) Config() config.Config {
	return a.cfg
}

func (a application) Store() store.Store {
	return a.store
}

func (a application) Service() service.Service {
	return a.service
}

func (a application) Logger() *slog.Logger {
	return a.logger
}

func (a application) shutdown() error {
	a.logger.Info("shutting down app")

	var rootErr error

	if err := a.srv.Shutdown(); err != nil {
		rootErr = fmt.Errorf("error while shutting down http server: %w; ", err)
	}

	return rootErr
}
