package web

type Error struct {
	Message    string
	StatusCode int
	Details    string
}

func (w Error) Error() string {
	return w.Details
}
