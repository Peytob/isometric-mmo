package dev.peytob.mmo.core.state

const val ENTRYPOINT_STATE: String = "ENTRYPOINT_STATE"

interface EngineState {

    fun getDebugName(): String = javaClass.simpleName

    fun isTerminated(): Boolean

    fun tick()
}
