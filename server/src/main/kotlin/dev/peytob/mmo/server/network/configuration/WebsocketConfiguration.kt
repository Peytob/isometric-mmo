package dev.peytob.mmo.server.network.configuration

import dev.peytob.mmo.server.network.handler.BaseWebSocketHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

private val log = LoggerFactory.getLogger(WebsocketConfiguration::class.java)

@Configuration
@EnableWebSocket
class WebsocketConfiguration(
    private val websocketHandlers: Collection<BaseWebSocketHandler<*>>
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        if (websocketHandlers.isEmpty()) {
            log.info("Websocket game handlers not found, skipping registration")
            return
        }

        log.info("Registering {} game websocket handlers", websocketHandlers.size)

        websocketHandlers.forEach { handler ->
            log.info(
                "Registering game websocket handler '{}' with path '{}'",
                handler.javaClass.simpleName,
                handler.path
            )
            registry.addHandler(handler, handler.path)
        }

        log.info("Game websocket handlers has been registered")
    }
}
