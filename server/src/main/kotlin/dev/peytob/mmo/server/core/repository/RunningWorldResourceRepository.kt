package dev.peytob.mmo.server.core.repository

import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.core.resource.repository.BaseResourceRepository
import dev.peytob.mmo.server.core.resource.RunningWorldResource
import dev.peytob.mmo.server.core.resource.WorldResource
import org.springframework.stereotype.Component

@Component
class RunningWorldResourceRepository : BaseResourceRepository<RunningWorldResource>() {
    private val worldRepositoryIndex = object : RepositoryIndex<ResourceId, RunningWorldResource>() {
        override fun extractKey(resource: RunningWorldResource): ResourceId = resource.world.id
    }

    init {
        this.appendIndex(worldRepositoryIndex)
    }

    fun findAllByWorld(world: WorldResource): Collection<RunningWorldResource> {
        return worldRepositoryIndex[world.id]
    }
}
