package dev.peytob.mmo.client.network.model

import dev.peytob.mmo.core.network.model.ConnectionType
import org.springframework.web.reactive.function.client.WebClient

interface ServerConnection {
    val backendWebClient: WebClient
    val connectionType: ConnectionType
}
