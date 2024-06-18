package dev.peytob.mmo.server.network.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.GenericTypeResolver.resolveTypeArgument
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

abstract class BaseWebSocketHandler<T> : TextWebSocketHandler() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val messageType = resolveTypeArgument(this.javaClass, BaseWebSocketHandler::class.java) as Class<*>

    abstract val path: String

    @Suppress("UNCHECKED_CAST")
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val deserializedMessage = objectMapper.readValue(message.payload, messageType)
        handleDeserializedMessage(session, deserializedMessage as T)
    }

    protected abstract fun handleDeserializedMessage(session: WebSocketSession, message: T)
}
