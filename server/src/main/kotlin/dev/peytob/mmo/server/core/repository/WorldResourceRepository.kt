package dev.peytob.mmo.server.core.repository

import dev.peytob.mmo.core.resource.repository.BaseResourceRepository
import dev.peytob.mmo.server.core.resource.WorldResource
import org.springframework.stereotype.Component

@Component
class WorldResourceRepository : BaseResourceRepository<WorldResource>()
