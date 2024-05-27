package dev.peytob.mmo.client

import dev.peytob.mmo.core.state.EngineCycleManager
import dev.peytob.mmo.core.utils.ExitCode
import org.springframework.stereotype.Service

@Service
class MmoClientEngine(
    private val engineCycleManager: EngineCycleManager
) {

    fun run(): ExitCode {

        while (isRunning()) {
            engineCycleManager.tick()
        }

        return ExitCode.SUCCESS
    }

    private fun isRunning(): Boolean = !engineCycleManager.getEngineState().isTerminated()
}