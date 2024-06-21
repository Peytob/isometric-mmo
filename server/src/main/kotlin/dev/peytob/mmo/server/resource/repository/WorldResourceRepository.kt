package dev.peytob.mmo.server.resource.repository

import dev.peytob.mmo.core.resource.repository.BaseResourceRepository
import dev.peytob.mmo.server.resource.instance.WorldResource
import org.springframework.stereotype.Component

@Component
class WorldResourceRepository : BaseResourceRepository<WorldResource>()
