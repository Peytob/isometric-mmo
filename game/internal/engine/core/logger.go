package core

import (
	"context"
	"fmt"
	"io"
	"log/slog"
	"os"
)

func LoadClientLogger(_ context.Context, cfg *Configuration) (*slog.Logger, error) {
	if !cfg.Log.Enabled {
		return slog.New(slog.NewTextHandler(io.Discard, nil)), nil
	}

	level, err := parseLoggingLevel(cfg.Log.Level)
	if err != nil {
		return nil, fmt.Errorf("failed to parse logging level %s: %v", cfg.Log.Level, err)
	}

	handler := slog.NewTextHandler(os.Stdout, &slog.HandlerOptions{
		Level: level,
	})

	return slog.New(handler), nil
}

func LoadServerLogger(_ context.Context, cfg *Configuration) (*slog.Logger, error) {
	if !cfg.Log.Enabled {
		return slog.New(slog.NewTextHandler(io.Discard, nil)), nil
	}

	level, err := parseLoggingLevel(cfg.Log.Level)
	if err != nil {
		return nil, fmt.Errorf("failed to parse logging level %s: %v", cfg.Log.Level, err)
	}

	handler := slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
		Level: level,
	})

	return slog.New(handler), nil
}

func parseLoggingLevel(s string) (slog.Level, error) {
	var level slog.Level
	var err = level.UnmarshalText([]byte(s))
	return level, err
}
