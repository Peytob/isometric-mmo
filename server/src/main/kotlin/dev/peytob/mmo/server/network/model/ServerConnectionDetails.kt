package dev.peytob.mmo.server.network.model

import java.net.URI

interface ServerConnectionDetails {
    val backendToken: String
    val selfUrl: URI
    val backendUrl: URI
}
