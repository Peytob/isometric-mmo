package dev.peytob.mmo.core.resource.service.loading.provider

interface DataLoadingProvider<R> {

    fun loadResources(): Collection<R>
}
