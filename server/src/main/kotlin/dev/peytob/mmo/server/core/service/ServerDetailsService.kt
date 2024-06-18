package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.server.core.model.ServerConnectionDetails

interface ServerDetailsService {

    fun getServerConnectionDetails(): ServerConnectionDetails
}
