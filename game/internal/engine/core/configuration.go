package core

import (
	"errors"
	"fmt"

	"github.com/go-playground/validator/v10"
	"github.com/ilyakaznacheev/cleanenv"
)

type Configuration struct {
	Log  *LogConfiguration  `yaml:"log" env-prefix:"LOG_" validate:"omitempty"`
	Http *HttpConfiguration `yaml:"http" env-prefix:"HTTP_" validate:"omitempty"`
}

type LogConfiguration struct {
	Enabled bool   `yaml:"enabled" env:"ENABLED"`
	Level   string `yaml:"level" env:"LEVEL" validate:"required,oneof=debug info warn error"`

	// Logging format. Used for client and server logging format separation.
	Handler int `yaml:"-" env:"-"`
}

type HttpConfiguration struct {
	Port int    `yaml:"port" env:"PORT" validate:"required,min=1,max=65535"`
	Host string `yaml:"host" env:"HOST" validate:"required"`
}

func LoadConfiguration(path string) (*Configuration, error) {
	cfg := &Configuration{}

	if err := cleanenv.ReadConfig(path, cfg); err != nil {
		return nil, fmt.Errorf("failed to load configuration %s from and envs: %w", path, err)
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
