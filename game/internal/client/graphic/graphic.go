package graphic

import "peytob/isometricmmo/game/internal/client/window"

type Graphic struct {
}

func Init(window window.Window) (Graphic, error) {
	//var err error
	var graphic Graphic

	return graphic, nil
}

func (g Graphic) Terminate() {
}
