package window

import (
	"fmt"
	"peytob/isometricmmo/game/internal/engine/core"
	"time"

	"github.com/go-gl/glfw/v3.3/glfw"
)

type Window struct {
	window *glfw.Window
}

func Init(configuration *core.WindowConfiguration) (Window, error) {
	var err error
	var window Window

	err = glfw.Init()
	if err != nil {
		return window, fmt.Errorf("failed to initialize GLFW: %w", err)
	}

	glfw.DefaultWindowHints()
	glfw.WindowHint(glfw.ContextVersionMajor, 3)
	glfw.WindowHint(glfw.ContextVersionMinor, 3)
	glfw.WindowHint(glfw.OpenGLProfile, glfw.OpenGLCoreProfile)
	glfw.WindowHint(glfw.Resizable, glfw.False)
	if window.window, err = glfw.CreateWindow(configuration.Width, configuration.Height, "Testing", nil, nil); err != nil {
		return window, fmt.Errorf("failed to create GLFW window: %w", err)
	}
	window.window.MakeContextCurrent()
	glfw.SwapInterval(int(time.Second.Milliseconds() / 60))

	return window, nil
}

func (w Window) Terminate() {
	w.window.Destroy()
}

func (w Window) Close() {
	w.window.SetShouldClose(true)
}

func (w Window) ShouldClose() bool {
	return w.window.ShouldClose()
}

func (w Window) PoolEvents() {
	glfw.PollEvents()
}

func (w Window) OnClose(callback func(Window)) {
	w.window.SetCloseCallback(func(_ *glfw.Window) {
		callback(w)
	})
}
