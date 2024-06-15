package dev.peytob.mmo.client.network.model.dto

import java.net.URI

data class ServerConnectionDetailsResult(
    val backendUrl: URI,
    val serverUrl: URI,
)
