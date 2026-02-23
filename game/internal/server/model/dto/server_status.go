package model

const (
	StatusHealthy   = "healthy"
	StatusUnhealthy = "unhealthy"
	StatusDisabled  = "disabled"
)

type ServerStatus struct {
	Status string `json:"status"`
}
