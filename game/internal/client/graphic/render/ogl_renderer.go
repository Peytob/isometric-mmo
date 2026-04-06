package render

import "github.com/go-gl/gl/v3.3-core/gl"

type oglRenderer struct {
}

func NewOglRenderer() Renderer {
	return &oglRenderer{}
}

func (o oglRenderer) NewCanvas() *Canvas {
	return &Canvas{}
}

func (o oglRenderer) RenderCanvas(canvas *Canvas, opts Opts) {
	//TODO implement me
}

func (o oglRenderer) Clear() {
	//TODO implement me
}

func (o oglRenderer) GetDetails() RendererDetails {
	return RendererDetails{
		Renderer: "OpenGL " + gl.GoStr(gl.GetString(gl.VERSION)),
		Device:   gl.GoStr(gl.GetString(gl.RENDERER)),
	}
}
