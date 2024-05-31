package dev.peytob.mmo.core.state

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

private var log = LoggerFactory.getLogger(EngineCycleManager::class.java)

@Service
class EngineCycleManager(
    @Qualifier(ENTRYPOINT_STATE)
    private var engineState: EngineState
) {

    private var nextEngineState: EngineState? = null

    fun tick() {
        engineState.tick()
    }

    fun scheduleChangeState(newState: EngineState) {
        nextEngineState = newState
    }

    fun performChangeState(): Boolean {
        val nextEngineState = nextEngineState
            ?: return false

        log.info("Changing engine state from {} to {}", engineState.getDebugName(), nextEngineState.getDebugName())

        engineState = nextEngineState

        return true
    }

    fun getEngineState(): EngineState = engineState
}
