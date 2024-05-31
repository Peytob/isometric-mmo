package dev.peytob.mmo.client.graphic.model.glfw

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11C.glViewport
import org.lwjgl.system.MemoryUtil.NULL
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(Window::class.java)

class Window private constructor(
    val pointer: Long,
    val width: Int,
    val height: Int
) : AutoCloseable {

    fun isShouldClose(): Boolean {
        return glfwWindowShouldClose(pointer)
    }

    fun setCloseFlag(flag: Boolean) {
        glfwSetWindowShouldClose(pointer, flag)
    }

    fun pollEvents() {
        glfwPollEvents()
    }

    fun display() {
        glfwSwapBuffers(pointer)
    }

    fun show() {
        glfwShowWindow(pointer)
    }

    override fun close() {
        destroy(pointer)
    }

    companion object {

        fun create(title: String, width: Int, height: Int): Window {
            log.info("Creating new GLFW window")
            glfwDefaultWindowHints()
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
            val pointer = glfwCreateWindow(width, height, title, NULL, NULL)
            glfwMakeContextCurrent(pointer)
            glfwSwapInterval(1)
            glfwSetInputMode(pointer, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
            GL.createCapabilities()

            // TODO Getting window framebuffer sizes
            glViewport(0, 0, width, height)
            log.info("Window ({}) has been created", pointer)
            return Window(pointer, width, height)
        }

        fun destroy(pointer: Long) {
            log.info("Destroying window ({})", pointer)
            glfwFreeCallbacks(pointer)
            glfwDestroyWindow(pointer)
            log.info("Window ({}) has been destroyed", pointer)
        }
    }
}