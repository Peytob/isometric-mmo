package dev.peytob.mmo.core.resource.service.loading.provider

import dev.peytob.mmo.core.resource.Resource

interface ResourceLoadingProvider<R : Resource> {

    fun loadResources(): Collection<R>
}
