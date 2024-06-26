package dev.peytob.mmo.server.core.initialization

import dev.peytob.mmo.core.resource.service.loading.ResourceLoader
import dev.peytob.mmo.server.core.service.DefaultExecutingWorldStarterService
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger(ServerInitializationEntrypoint::class.java)

@Component
class ServerInitializationEntrypoint(
    private val resourcesLoaders: Collection<ResourceLoader<*>>,
    private val defaultExecutingWorldStarterService: DefaultExecutingWorldStarterService
) {

    @EventListener(ApplicationReadyEvent::class)
    fun initializeServer() {
        log.info("Initializing server state")

        log.info("Performing resources loading")
        resourcesLoaders.forEach(ResourceLoader<*>::loadResources)

        log.info("Starting mock worlds")
        defaultExecutingWorldStarterService.startDefaultWorlds()

        log.info("Server has been loaded and initialized successfully!")
    }
}
