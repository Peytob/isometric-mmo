package dev.peytob.mmo.server.resource.service.loading.provider

import dev.peytob.mmo.core.resource.service.loading.provider.ResourceLoadingProvider
import dev.peytob.mmo.server.resource.instance.WorldResource
import org.springframework.stereotype.Component

@Component
class MockWorldResourceLoadingProvider : ResourceLoadingProvider<WorldResource> {

    override fun loadResources(): Collection<WorldResource> {
        val worldResource = WorldResource(
            id = "mock_world"
        )

        return listOf(worldResource)
    }
}
