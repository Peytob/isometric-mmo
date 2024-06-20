package dev.peytob.mmo.core.resource.service.loading

import dev.peytob.mmo.core.resource.Resource

interface ResourceLoader<R : Resource> {

    fun loadResources(): Collection<R>
}