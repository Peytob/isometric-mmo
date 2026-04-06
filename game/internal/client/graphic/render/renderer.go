package render

import "peytob/isometricmmo/game/internal/client/graphic/gmodel"

// Renderer Used for rendering by a specific implementation of a graphics library.
type Renderer interface {
	// NewCanvas Creates new canvas instance
	NewCanvas() *Canvas

	// RenderCanvas Renders canvas as separated mesh
	RenderCanvas(canvas *Canvas, renderOpts Opts)

	// Clear Clears render target
	Clear()

	// GetDetails Collects and returns details about rendering software and hardware
	GetDetails() RendererDetails
}

type Opts struct {
	// Target Optional value to use non-default render target
	Target gmodel.RenderTarget

	// Shader Required value. ShaderProgram, used to render canvas
	Shader gmodel.ShaderProgram
}

type RendererDetails struct {
	// Name of the software renderer with implementation detail.
	// Example: 'OpenGL 3.3'
	Renderer string

	// Name of the renderer device. Usually just GPU name with some details.
	// Example: 'AMD Radeon Graphics'
	Device string
}
