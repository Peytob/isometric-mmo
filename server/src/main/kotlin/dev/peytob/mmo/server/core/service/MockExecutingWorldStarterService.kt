package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.server.core.repository.WorldResourceRepository
import org.springframework.stereotype.Service

@Service
class MockExecutingWorldStarterService(
    private val worldResourceRepository: WorldResourceRepository,
    private val executingWorldService: ExecutingWorldService
) {

    fun startMockWorlds() {
        val world = worldResourceRepository["mock_world"]!!
        executingWorldService.startExecutingWorld(world)
    }
}