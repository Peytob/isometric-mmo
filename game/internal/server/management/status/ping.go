package status

import (
	"peytob/isometricmmo/game/internal/engine/core"
	"peytob/isometricmmo/game/internal/server/model/dto"
)

func GetServerStatus(_ core.AppContext) dto.ServerStatus {
	return dto.ServerStatus{
		Status: dto.StatusHealthy,
	}
}
