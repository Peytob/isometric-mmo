package dev.peytob.mmo.server.network.handler

import dev.peytob.mmo.core.network.interfaces.CharacterEnterCommand
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class CharacterEnterWebsocketHandler : BaseWebSocketHandler<CharacterEnterCommand>() {

    override val path: String
        get() = "/character/enter"

    override fun handleDeserializedMessage(session: WebSocketSession, message: CharacterEnterCommand) {
    }
}
