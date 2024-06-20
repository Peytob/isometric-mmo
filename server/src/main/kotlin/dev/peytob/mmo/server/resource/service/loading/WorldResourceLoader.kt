package dev.peytob.mmo.server.resource.service.loading

import dev.peytob.mmo.core.resource.repository.ResourceRepository
import dev.peytob.mmo.core.resource.service.loading.AbstractResourceLoader
import dev.peytob.mmo.core.resource.service.loading.provider.ResourceLoadingProvider
import dev.peytob.mmo.server.resource.instance.WorldResource
import org.springframework.stereotype.Service

@Service
class WorldResourceLoader(
    resourceLoadingProviders: Collection<ResourceLoadingProvider<WorldResource>>,
    resourceRepository: ResourceRepository<WorldResource>
) : AbstractResourceLoader<WorldResource>(
    resourceLoadingProviders,
    resourceRepository
)
