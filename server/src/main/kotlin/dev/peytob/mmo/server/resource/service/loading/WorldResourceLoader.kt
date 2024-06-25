package dev.peytob.mmo.server.resource.service.loading

import dev.peytob.mmo.core.resource.repository.ResourceRepository
import dev.peytob.mmo.core.resource.service.loading.AbstractResourceLoader
import dev.peytob.mmo.core.resource.service.loading.provider.DataLoadingProvider
import dev.peytob.mmo.server.core.resource.WorldResource
import org.springframework.stereotype.Service

@Service
class WorldResourceLoader(
    dataLoadingProviders: Collection<DataLoadingProvider<WorldResource>>,
    resourceRepository: ResourceRepository<WorldResource>
) : AbstractResourceLoader<WorldResource>(
    dataLoadingProviders,
    resourceRepository
)
