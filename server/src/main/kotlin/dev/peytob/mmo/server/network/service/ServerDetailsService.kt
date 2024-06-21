package dev.peytob.mmo.server.network.service

import dev.peytob.mmo.server.network.model.ServerConnectionDetails

interface ServerDetailsService {

    fun getServerConnectionDetails(): ServerConnectionDetails
}
