package dev.peytob.mmo.server.core.service

import dev.peytob.ecs.context.EcsContextBuilder
import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.server.core.repository.RunningWorldResourceRepository
import dev.peytob.mmo.server.core.resource.RunningWorldResource
import dev.peytob.mmo.server.core.resource.WorldResource
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(RunningWorldService::class.java)

@Service
class RunningWorldService(
    private val runningWorldResourceRepository: RunningWorldResourceRepository,
    private val worldExecutingService: WorldExecutingService,
) {

    fun startRunningWorld(world: WorldResource): RunningWorldResource {
        val executingWorldId = generateNewRunningWorldId(world)

        log.info("Creating new executing world instance from world {}. New resource id {}", world.id, executingWorldId)

        val executingWorld = RunningWorldResource(
            id = executingWorldId,
            world = world,
            // TODO Construct context from world
            ecsContext = EcsContextBuilder().build()
        )

        runningWorldResourceRepository.append(executingWorld)
        worldExecutingService.startRunningWorldExecuting(executingWorld)

        return executingWorld
    }

    fun stopRunningWorld(runningWorld: RunningWorldResource) {
        log.info("Removing executing world with id {}", runningWorld.id)

        val isExecutingWorldRemoved = runningWorldResourceRepository.remove(runningWorld)

        if (!isExecutingWorldRemoved) {
            log.error("Executing world with id {} not found while removing", runningWorld.id)
            return
        }

        worldExecutingService.stopRunningWorldExecuting(runningWorld)

        log.info("Executing world with id {} has been removed", runningWorld.id)
    }

    fun getActiveExecutingWorlds(): Collection<RunningWorldResource> {
        return runningWorldResourceRepository.getAll()
    }

    private fun generateNewRunningWorldId(world: WorldResource): ResourceId {
        return "${world.id}_${RandomStringUtils.randomNumeric(6)}"
    }
}
