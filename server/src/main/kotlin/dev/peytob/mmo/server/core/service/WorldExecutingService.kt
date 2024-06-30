package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.core.resource.exception.ResourceConflictException
import dev.peytob.mmo.server.core.repository.ExecutingWorldResourceRepository
import dev.peytob.mmo.server.core.resource.ExecutingWorldResource
import dev.peytob.mmo.server.core.resource.RunningWorldResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean

private val log = LoggerFactory.getLogger(WorldExecutingService::class.java)

@Service
class WorldExecutingService(
    private val executingWorldResourceRepository: ExecutingWorldResourceRepository
) {

    fun startRunningWorldExecuting(runningWorld: RunningWorldResource) {
        log.info("Starting async executing cycle for executing world {}", runningWorld.id)

        if (executingWorldResourceRepository.containsByRunningWorld(runningWorld)) {
            throw ResourceConflictException("Running world {} already has executing process")
        }

        val stopExecutingFlag = AtomicBoolean(false)

        val executingWorld = ExecutingWorldResource(
            id = runningWorld.id,
            runningWorld = runningWorld,
            stopExecutingFlag = stopExecutingFlag,
            executingFuture = CompletableFuture.runAsync(WorldExecutionCycle(runningWorld, stopExecutingFlag))
        )

        executingWorldResourceRepository.append(executingWorld)
    }

    fun stopRunningWorldExecuting(runningWorld: RunningWorldResource) {
        log.info("Terminating async executing cycle for executing world {}", runningWorld.id)

        val executingWorld = (executingWorldResourceRepository.getByRunningWorld(runningWorld)
            ?: throw ResourceConflictException("Running world {} already has executing process"))

        executingWorld.stopExecutingFlag.set(true)
        executingWorld.executingFuture.get()

        executingWorldResourceRepository.remove(executingWorld)
    }
}
