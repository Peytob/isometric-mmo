package main

import (
	"peytob/isometricmmo/backend/config"
	"peytob/isometricmmo/backend/internal/app"
)

func main() {
	var err error

	cfg, err := config.LoadConfig()
	if err != nil {
		panic("failed to load configuration: " + err.Error())
	}

	application, err := app.Initialize(cfg)
	if err != nil {
		panic("failed to initialize app: " + err.Error())
	}

	err = application.Run()
	if err != nil {
		panic("unhandled error during executing: " + err.Error())
	}
}
