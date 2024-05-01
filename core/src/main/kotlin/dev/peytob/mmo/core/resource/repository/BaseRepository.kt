package dev.peytob.mmo.core.resource.repository

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.ResourceId

abstract class BaseRepository<R : Resource> : Repository<R> {

    private val resourcesById: MutableMap<ResourceId, R> = mutableMapOf()

    private val repositoryIndices: Collection<RepositoryIndex<*, R>> = mutableListOf()

    override fun get(resourceId: ResourceId): R? = resourcesById.get(textId)

    override fun getAll(): Collection<R> = resourcesById.values

    override fun getCount(): Int = resourcesById.size

    override fun contains(resourceId: ResourceId): Boolean = resourcesById.containsKey(resourceId)

    override fun append(resource: R): Boolean {
        if (contains(resource)) {
            return false
        }

        resourcesById.put(resource.id, resource)
        repositoryIndices.forEach { index -> index.append(resource) }
        return true
    }

    override fun remove(resource: R): Boolean {
        if (contains(resource)) {
            return false
        }

        resourcesById.remove(resource.id, resource)
        repositoryIndices.forEach { index -> index.remove(resource) }
        return true
    }


    protected abstract class RepositoryIndex<K, R> {

        private val resourceByKey: Multimap<K, R> = HashMultimap.create()

        private val repositoryIndices: Collection<RepositoryIndex<*>> = mutableListOf()

        fun append(resource: R) {
            val key = extractKey(resource)
            resourceByKey.put(key, resource)
        }

        fun remove(resource: R) {
            val key = extractKey(resource)
            resourceByKey.remove(key, resource)
        }

        operator fun contains(resource: R): Boolean {
            val key = extractKey(resource)
            return contains(key)
        }

        operator fun contains(key: K): Boolean {
            return resourceByKey.containsKey(key)
        }

        fun getSingle(key: K): R? {
            val resourceList: List<R> = resourceByKey.get(key)
            return if (resourceList.isEmpty()) null else resourceList[0]
        }

        operator fun get(key: K): Collection<R> {
            return resourceByKey.get(key)
        }

        protected abstract fun extractKey(resource: R): K
    }
}