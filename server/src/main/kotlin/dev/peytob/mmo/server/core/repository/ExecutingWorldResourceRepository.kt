package dev.peytob.mmo.server.core.repository

import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.core.resource.repository.BaseResourceRepository
import dev.peytob.mmo.server.core.resource.ExecutingWorldResource
import dev.peytob.mmo.server.core.resource.RunningWorldResource
import org.springframework.stereotype.Component

@Component
class ExecutingWorldResourceRepository : BaseResourceRepository<ExecutingWorldResource>() {
    private val runningWorldRepositoryIndex = object : RepositoryIndex<ResourceId, ExecutingWorldResource>() {
        override fun extractKey(resource: ExecutingWorldResource): ResourceId = resource.runningWorld.id
    }

    init {
        this.appendIndex(runningWorldRepositoryIndex)
    }

    fun containsByRunningWorld(runningWorld: RunningWorldResource): Boolean {
        return runningWorldRepositoryIndex.contains(runningWorld.id)
    }

    fun getByRunningWorld(runningWorld: RunningWorldResource): ExecutingWorldResource? {
        return runningWorldRepositoryIndex.getSingle(runningWorld.id)
    }
}
