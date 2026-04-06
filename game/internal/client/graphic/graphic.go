package graphic

import (
	"fmt"
	"github.com/go-gl/gl/v3.3-core/gl"
	"peytob/isometricmmo/game/internal/client/graphic/render"
	"peytob/isometricmmo/game/internal/client/window"
)

type Graphic struct {
	r render.Renderer
}

func Init(window window.Window) (Graphic, error) {
	//var err error
	var graphic Graphic

	if err := gl.Init(); err != nil {
		return graphic, fmt.Errorf("failed to graphic: %w", err)
	}
	graphic.r = render.NewOglRenderer()

	return graphic, nil
}

func (g Graphic) Terminate() {
}

func (g Graphic) Renderer() render.Renderer {
	return g.r
}
