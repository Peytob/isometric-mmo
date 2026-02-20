package network

type WebError struct {
	Message    string
	StatusCode int
	Details    string
}

func (w WebError) Error() string {
	return w.Details
}
