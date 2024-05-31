package dev.peytob.mmo.core.state

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class EngineCycleManager(
    @Qualifier(ENTRYPOINT_STATE)
    private var engineState: EngineState
) {

    fun tick() {
        engineState.tick()
    }

    fun changeState(newState: EngineState) {
        engineState = newState
    }

    fun getEngineState(): EngineState = engineState
}
