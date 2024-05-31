package dev.peytob.mmo.client.graphic

import dev.peytob.mmo.client.graphic.model.glfw.Window
import dev.peytob.mmo.core.engine.EngineModule
import org.springframework.stereotype.Component

@Component
class GraphicEngineModule(
    private val window: Window
) : EngineModule {

    override fun initialize() {
        window.show()
    }

    override fun destroy() {
        // Empty
    }

    override fun getName(): String = "Graphic"
}
