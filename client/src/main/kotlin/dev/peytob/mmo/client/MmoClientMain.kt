package dev.peytob.mmo.client

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext

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

fun initializeGlfw() {
    log.info("Initializing GLFW library")
    GLFWErrorCallback.createPrint(System.err).set()

    val isGlfwInitialized = glfwInit()
    if (!isGlfwInitialized) {
        throw IllegalStateException("Unable to initialize GLFW")
    }
}

fun destroyGlfw() {
    log.info("Destroying GLFW library")
    glfwTerminate()
    glfwSetErrorCallback(null)?.free()
}

