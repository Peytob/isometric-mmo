package dev.peytob.mmo.core.resource.service.loading

import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.repository.ResourceRepository
import dev.peytob.mmo.core.resource.service.loading.provider.ResourceLoadingProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.GenericTypeResolver.resolveTypeArgument

abstract class AbstractResourceLoader<R : Resource>(
    private val resourceLoadingProviders: Collection<ResourceLoadingProvider<R>>,
    private val resourceRepository: ResourceRepository<R>
) : ResourceLoader<R> {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private val resourceType = resolveTypeArgument(this.javaClass, AbstractResourceLoader::class.java) as Class<*>

    override fun loadResources(): Collection<R> {
        log.info("Loading resources of type {} by {}", resourceType.simpleName, this.javaClass.simpleName)

        return resourceLoadingProviders
            .map {
                val loadedResources = it.loadResources()
                log.info("Loaded {} resources of type {} from resource provider {}", loadedResources.size, resourceType.simpleName, it.javaClass.simpleName)
                loadedResources
            }
            .onEach {
                it.forEach(resourceRepository::append)
            }
            .flatten()
    }
}
