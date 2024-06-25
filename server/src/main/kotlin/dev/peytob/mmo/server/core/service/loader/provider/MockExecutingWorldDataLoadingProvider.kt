package dev.peytob.mmo.server.core.service.loader.provider

import dev.peytob.mmo.core.resource.service.loading.provider.DataLoadingProvider
import dev.peytob.mmo.server.core.resource.WorldResource
import org.springframework.stereotype.Component

@Component
class MockExecutingWorldDataLoadingProvider : DataLoadingProvider<WorldResource> {

    override fun loadResources(): Collection<WorldResource> {
        val worldResource = WorldResource(
            id = "mock_world"
        )

        return listOf(worldResource)
    }
}
