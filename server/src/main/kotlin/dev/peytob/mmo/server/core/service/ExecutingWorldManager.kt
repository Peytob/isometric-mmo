package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.core.resource.service.ResourceIdGenerator
import dev.peytob.mmo.server.core.repository.ExecutingWorldResourceRepository
import dev.peytob.mmo.server.core.resource.ExecutingWorldResource
import dev.peytob.mmo.server.core.resource.WorldResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(ExecutingWorldManager::class.java)

@Service
class ExecutingWorldManager(
    private val executingWorldResourceRepository: ExecutingWorldResourceRepository,
    private val executingWorldExecutionService: ExecutingWorldExecutionService,
    private val resourceIdGenerator: ResourceIdGenerator
) {

    fun startExecutingWorld(world: WorldResource): ExecutingWorldResource {
        val executingWorldId = resourceIdGenerator.generateResourceId()

        log.info("Creating new executing world instance from world {}. New resource id {}", world.id, executingWorldId)

        val executingWorld = ExecutingWorldResource(
            id = executingWorldId,
            world = world,
            state = ExecutingWorldResource.ExecutingWorldState.EXECUTING
        )

        executingWorldResourceRepository.append(executingWorld)
        executingWorldExecutionService.startExecutionWorldExecuting(executingWorld)

        return executingWorld
    }

    fun stopExecutingWorld(executingWorld: ExecutingWorldResource) {
        log.info("Removing executing world with id {}", executingWorld.id)

        val isExecutingWorldRemoved = executingWorldResourceRepository.remove(executingWorld)

        if (!isExecutingWorldRemoved) {
            log.error("Executing world with id {} not found while removing", executingWorld.id)
            return
        }

        // Some clean up logic

        log.info("Executing world with id {} has been removed", executingWorld.id)
    }

    fun getActiveExecutingWorlds(): Collection<ExecutingWorldResource> {
        return executingWorldResourceRepository.getAll()
    }
}
