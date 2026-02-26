package graphic

import (
	"fmt"

	"github.com/go-gl/glfw/v3.3/glfw"
)

type Graphic struct {
	window *glfw.Window
}

func InitGraphic() (Graphic, error) {
	var err error
	var graphic Graphic

	err = glfw.Init()
	if err != nil {
		return graphic, fmt.Errorf("failed to initialize GLFW: %w", err)
	}

	glfw.WindowHint(glfw.ContextVersionMajor, 3)
	glfw.WindowHint(glfw.ContextVersionMinor, 3)
	glfw.WindowHint(glfw.OpenGLProfile, glfw.OpenGLCoreProfile)
	glfw.WindowHint(glfw.Resizable, glfw.False)
	if graphic.window, err = glfw.CreateWindow(640, 480, "Testing", nil, nil); err != nil {
		return graphic, fmt.Errorf("failed to create GLFW window: %w", err)
	}

	return graphic, nil
}

func (g Graphic) Terminate() {
	g.window.Destroy()
}
