package dev.peytob.mmo.client

import dev.peytob.mmo.client.network.service.management.ConnectionManager
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import java.net.URI

@SpringBootApplication
class MmoClientMain

private val log = LoggerFactory.getLogger(MmoClientMain::class.java)

fun main(args: Array<String>) {
    log.info("Starting MMO client application")

    initializeStaticCLibraries()

    val context: ConfigurableApplicationContext = SpringApplicationBuilder(MmoClientMain::class.java)
        .web(WebApplicationType.NONE)
        .bannerMode(Banner.Mode.OFF)
        .run(*args)

    val clientEngine = context.getBean(MmoClientEngine::class.java)

    val connectionManager = context.getBean(ConnectionManager::class.java)
    connectionManager.connectToServer(URI.create("http://localhost:8081/"), "", "")

    clientEngine.run()

    destroyStaticCLibraries()
}

private fun initializeStaticCLibraries() {
    log.info("Initializing static C libraries")
    initializeGlfw()
    log.info("Static C libraries has been initialized")
}

private fun destroyStaticCLibraries() {
    log.info("Destroying static C libraries")
    destroyGlfw()
    log.info("Static C libraries has been destroyed")
}

private fun initializeGlfw() {
    log.info("Initializing GLFW library")
    GLFWErrorCallback.createPrint(System.err).set()

    val isGlfwInitialized = glfwInit()
    if (!isGlfwInitialized) {
        throw IllegalStateException("Unable to initialize GLFW")
    }
}

private fun destroyGlfw() {
    log.info("Destroying GLFW library")
    glfwTerminate()
    glfwSetErrorCallback(null)?.free()
}

