package dev.peytob.mmo.server.core.model

import java.net.URI

interface ServerConnectionDetails {
    val selfUrl: URI
    val backendUrl: URI
}
