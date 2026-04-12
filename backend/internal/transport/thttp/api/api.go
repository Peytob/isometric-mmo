package api

import (
	"context"
	"log/slog"
	"net/http"
	"peytob/isometricmmo/backend/config"
	"peytob/isometricmmo/backend/internal/service"
)

func NewApi(ctx context.Context, logger *slog.Logger, cfg config.Config, service service.Service) (http.Handler, error) {
	return http.NewServeMux(), nil
}
