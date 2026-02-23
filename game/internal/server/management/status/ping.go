package status

import (
	"peytob/isometricmmo/game/internal/engine/core"
	model "peytob/isometricmmo/game/internal/server/model/dto"
)

func GetServerStatus(_ core.AppContext) model.ServerStatus {
	return model.ServerStatus{
		Status: model.StatusHealthy,
	}
}
