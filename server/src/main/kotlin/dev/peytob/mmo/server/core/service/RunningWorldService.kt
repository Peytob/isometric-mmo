package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.core.resource.service.ResourceIdGenerator
import dev.peytob.mmo.server.core.repository.RunningWorldResourceRepository
import dev.peytob.mmo.server.core.resource.RunningWorldResource
import dev.peytob.mmo.server.core.resource.WorldResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(RunningWorldService::class.java)

@Service
class RunningWorldService(
    private val runningWorldResourceRepository: RunningWorldResourceRepository,
    private val worldExecutingService: WorldExecutingService,
    private val resourceIdGenerator: ResourceIdGenerator
) {

    fun startExecutingWorld(world: WorldResource): RunningWorldResource {
        val executingWorldId = resourceIdGenerator.generateResourceId()

        log.info("Creating new executing world instance from world {}. New resource id {}", world.id, executingWorldId)

        val executingWorld = RunningWorldResource(
            id = executingWorldId,
            world = world
        )

        runningWorldResourceRepository.append(executingWorld)
        worldExecutingService.startRunningWorldExecuting(executingWorld)

        return executingWorld
    }

    fun stopExecutingWorld(executingWorld: RunningWorldResource) {
        log.info("Removing executing world with id {}", executingWorld.id)

        val isExecutingWorldRemoved = runningWorldResourceRepository.remove(executingWorld)

        if (!isExecutingWorldRemoved) {
            log.error("Executing world with id {} not found while removing", executingWorld.id)
            return
        }

        worldExecutingService.stopRunningWorldExecuting(executingWorld)

        log.info("Executing world with id {} has been removed", executingWorld.id)
    }

    fun getActiveExecutingWorlds(): Collection<RunningWorldResource> {
        return runningWorldResourceRepository.getAll()
    }
}
