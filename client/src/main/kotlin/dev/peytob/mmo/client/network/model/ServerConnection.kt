package dev.peytob.mmo.client.network.model

import dev.peytob.mmo.core.network.model.ConnectionType
import org.springframework.web.reactive.function.client.WebClient

data class ServerConnection(
    val backendWebClient: WebClient,
    val serverWebClient: WebClient,
    val connectionType: ConnectionType,
    val connectionDetails: ServerConnectionDetails
)
