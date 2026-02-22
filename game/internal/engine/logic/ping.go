package logic

import (
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/engine/model"
)

func PingServer(_ core.AppContext) model.ServerStatus {
	return model.ServerStatus{
		Status: model.StatusHealthy,
	}
}
