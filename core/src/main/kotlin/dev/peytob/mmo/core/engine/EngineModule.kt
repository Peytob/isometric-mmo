package dev.peytob.mmo.core.engine

interface EngineModule {

    fun initialize()

    fun destroy()

    fun getName(): String
}