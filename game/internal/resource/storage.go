package resource

type Storage interface {
}

type clientStorage struct {
}

type serverStorage struct {
}

func NewClientStorage() Storage {
	return clientStorage{}
}

func NewServerStorage() Storage {
	return serverStorage{}
}
