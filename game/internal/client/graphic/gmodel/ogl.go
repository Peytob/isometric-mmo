package gmodel

type BaseOglResource struct {
	// Resource Id inside OpenGL
	Id uint32

	// Human-readability resource name. Usefully for debug or dumps
	Name string
}

type ShaderProgram struct {
	BaseOglResource
}

type Texture struct {
	BaseOglResource
}

type RenderTarget struct {
	BaseOglResource
}
