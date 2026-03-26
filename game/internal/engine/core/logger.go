package core

import (
	"context"
	"io"
	"log/slog"
	"os"
)

func LoadClientLogger(_ context.Context, cfg *LogConfiguration) (*slog.Logger, error) {
	if !cfg.Enabled {
		return slog.New(slog.NewTextHandler(io.Discard, nil)), nil
	}

	handler := slog.NewTextHandler(os.Stdout, &slog.HandlerOptions{
		Level: cfg.Level,
	})

	return slog.New(handler), nil
}

func LoadServerLogger(_ context.Context, cfg *LogConfiguration) (*slog.Logger, error) {
	if !cfg.Enabled {
		return slog.New(slog.NewTextHandler(io.Discard, nil)), nil
	}

	handler := slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
		Level: cfg.Level,
	})

	return slog.New(handler), nil
}
