package dev.peytob.mmo.core.resource.repository

import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.ResourceId

interface Repository<R : Resource> {

    operator fun get(resourceId: ResourceId): R?

    fun getAll(): Collection<R>

    fun getCount(): Int

    operator fun contains(resourceId: ResourceId): Boolean

    fun append(resource: R): Boolean

    fun remove(resource: R): Boolean
}
