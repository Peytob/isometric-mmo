package service

import "peytob/isometricmmo/backend/internal/store"

type Service struct {
}

func Initialize(_ store.Store) (Service, error) {
	return Service{}, nil
}
