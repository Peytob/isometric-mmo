package dev.peytob.mmo.core.resource.service

import dev.peytob.mmo.core.resource.Resource

interface ResourceLoader<R : Resource> {

    fun loadResources(): Collection<R>
}