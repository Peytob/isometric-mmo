package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.server.core.repository.WorldResourceRepository
import org.springframework.stereotype.Service

@Service
class DefaultExecutingWorldStarterService(
    private val worldResourceRepository: WorldResourceRepository,
    private val executingWorldManager: ExecutingWorldManager
) {

    fun startDefaultWorlds() {
        val world = worldResourceRepository["mock_world"]!!
        executingWorldManager.startExecutingWorld(world)
    }
}