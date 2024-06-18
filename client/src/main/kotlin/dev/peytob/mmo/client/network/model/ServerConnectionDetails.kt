package dev.peytob.mmo.client.network.model

import java.net.URI

data class ServerConnectionDetails(
    val serverUrl: URI,
    val backendUrl: URI
)
