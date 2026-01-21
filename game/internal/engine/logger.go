package engine

import (
	"context"
	"errors"
	"fmt"
	"io"
	"log/slog"
	"os"
)

func LoadLogger(_ context.Context, cfg *Configuration) (*slog.Logger, error) {
	if !cfg.Log.Enabled {
		return slog.New(slog.NewTextHandler(io.Discard, nil)), nil
	}

	level, err := parseLoggingLevel(cfg.Log.Level)
	if err != nil {
		return nil, fmt.Errorf("failed to parse logging level %s: %v", cfg.Log.Level, err)
	}

	handler, err := resolveHandler(cfg.Log.Handler, &slog.HandlerOptions{
		Level: level,
	})
	if err != nil {
		return nil, fmt.Errorf("failed to resolve handler: %v", err)
	}

	return slog.New(handler), nil
}

func parseLoggingLevel(s string) (slog.Level, error) {
	var level slog.Level
	var err = level.UnmarshalText([]byte(s))
	return level, err
}

func resolveHandler(handler int, opts *slog.HandlerOptions) (slog.Handler, error) {
	switch handler {
	case JsonLoggerFormat:
		return slog.NewJSONHandler(os.Stdout, opts), nil
	case TextLoggerFormat:
		return slog.NewTextHandler(os.Stdout, opts), nil
	default:
		return nil, errors.New("invalid log format")
	}
}
