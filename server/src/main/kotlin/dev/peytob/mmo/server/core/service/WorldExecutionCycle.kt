package dev.peytob.mmo.server.core.service

import dev.peytob.ecs.context.EcsContext
import dev.peytob.mmo.server.core.resource.RunningWorldResource
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

class WorldExecutionCycle(
    private val targetRunningWorld: RunningWorldResource,
    private val stopExecutingFlag: AtomicBoolean
) : Runnable {

    private val log = LoggerFactory.getLogger("Execution cycle ${targetRunningWorld.id}")

    override fun run() {
        log.info("Starting running world {} executing cycle", targetRunningWorld.id)

        while (!stopExecutingFlag.get()) {
            try {
                executeSystems(targetRunningWorld.ecsContext)
                Thread.yield()
            } catch (exception: Exception) {
                log.error("Unhandled exception while executing running world {} cycle", targetRunningWorld.id)
            }
        }

        log.info("Cycle has been executed")
    }

    private fun executeSystems(ecsContext: EcsContext) {
        ecsContext.getSystems().forEach { it.execute(ecsContext) }
    }
}
