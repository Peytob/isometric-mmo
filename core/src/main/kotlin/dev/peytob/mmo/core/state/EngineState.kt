package dev.peytob.mmo.core.state

const val ENTRYPOINT_STATE: String = "ENTRYPOINT_STATE"

interface EngineState {

    fun isTerminated(): Boolean

    fun tick()
}