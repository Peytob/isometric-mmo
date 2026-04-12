package config

import (
	"fmt"
	"log/slog"

	"github.com/caarlos0/env/v11"
	"github.com/go-playground/validator/v10"
)

type Config struct {
	Http Http `envPrefix:"HTTP_"`
	Log  Log  `envPrefix:"LOG_"`
}

type Http struct {
	Port int `env:"PORT" envDefault:"8081" validate:"required,min=1,max=65535"`
}

type Log struct {
	Level slog.Level `env:"LEVEL" envDefault:"INFO"`
}

func LoadConfig() (Config, error) {
	var err error

	cfg, err := env.ParseAsWithOptions[Config](env.Options{
		Prefix: "BMMO_",
	})
	if err != nil {
		return cfg, fmt.Errorf("config load error: %w", err)
	}

	err = validator.New().Struct(cfg)
	if err != nil {
		return cfg, fmt.Errorf("config validation error: %w", err)
	}

	return cfg, nil
}
