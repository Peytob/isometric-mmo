package utils

type Set[T comparable] map[T]struct{}

func NewSet[T comparable](size int) Set[T] {
	return make(Set[T], size)
}

func (s Set[T]) Contains(value T) bool {
	_, ok := s[value]
	return ok
}

func (s Set[T]) Add(value T) {
	s[value] = struct{}{}
}

func (s Set[T]) Remove(value T) {
	delete(s, value)
}
