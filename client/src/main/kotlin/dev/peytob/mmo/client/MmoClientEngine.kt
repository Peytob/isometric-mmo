package dev.peytob.mmo.client

import dev.peytob.mmo.core.engine.EngineModule
import dev.peytob.mmo.core.state.EngineCycleManager
import dev.peytob.mmo.core.utils.ExitCode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(MmoClientEngine::class.java)

@Service
class MmoClientEngine(
    private val engineCycleManager: EngineCycleManager,
    private val engineModules: Collection<EngineModule>
) {

    fun run(): ExitCode {
        initialize()

        while (isRunning()) {
            engineCycleManager.performChangeState()
            engineCycleManager.tick()
        }

        return ExitCode.SUCCESS
    }

    private fun initialize() {
        log.info("Initializing base modules")
        engineModules.forEach {
            log.info("Running engine module initializer: {}", it.getName())
            it.initialize()
        }
        log.info("Base modules has been initialized")
    }

    private fun isRunning(): Boolean = !engineCycleManager.getEngineState().isTerminated()
}
