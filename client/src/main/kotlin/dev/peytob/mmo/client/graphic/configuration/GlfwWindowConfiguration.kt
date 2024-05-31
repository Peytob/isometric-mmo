package dev.peytob.mmo.client.graphic.configuration

import dev.peytob.mmo.client.graphic.model.glfw.Window
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GlfwWindowConfiguration {

    @Bean
    fun window(): Window {
        return Window.create("Isometric MMO", 800, 600)
    }
}