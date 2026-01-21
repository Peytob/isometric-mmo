package engine

import (
	"context"
	"errors"
	"fmt"

	"github.com/go-playground/validator/v10"
	"github.com/ilyakaznacheev/cleanenv"
)

const (
	JsonLoggerFormat = iota
	TextLoggerFormat
)

type Configuration struct {
	Log *LogConfiguration `yaml:"log" env-prefix:"LOG_"`
}

type LogConfiguration struct {
	Enabled bool   `yaml:"enabled" env:"ENABLED"`
	Level   string `yaml:"level" env:"LEVEL" validate:"required,oneof=debug info warn error"`

	// Logging format. Used for client and server logging format separation.
	Handler int `yaml:"-" env:"-"`
}

func LoadClientConfiguration(ctx context.Context) (*Configuration, error) {
	cfg, err := loadConfiguration(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed client configuration loading: %w", err)
	}

	cfg.Log.Handler = TextLoggerFormat

	return cfg, nil
}

func loadConfiguration(_ context.Context) (*Configuration, error) {
	cfg := &Configuration{}

	if err := cleanenv.ReadConfig("./config/config.yaml", cfg); err != nil {
		return nil, fmt.Errorf("failed to load configuration from config.yaml and envs: %w", err)
	}

	if err := validateConfiguration(cfg); err != nil {
		return nil, fmt.Errorf("failed client configuration validation: %w", err)
	}

	return cfg, nil
}

func validateConfiguration(cfg *Configuration) error {
	validate := validator.New()

	err := validate.Struct(cfg)
	if err != nil {
		var validateErrs validator.ValidationErrors
		if errors.As(err, &validateErrs) {
			// TODO Return list of invalid fields
		}

		return err
	}

	return nil
}
