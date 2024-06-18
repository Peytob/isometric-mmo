package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.server.core.model.ServerConnectionDetails
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.net.URI

@Service
@EnableConfigurationProperties(RemoteServerDetailsService.ServerConnectionDetailsProperties::class)
class RemoteServerDetailsService(
    private val serverConnectionDetailsProperties: ServerConnectionDetailsProperties
) : ServerDetailsService {

    override fun getServerConnectionDetails(): ServerConnectionDetails = serverConnectionDetailsProperties

    @ConfigurationProperties(prefix = "connection.details")
    data class ServerConnectionDetailsProperties (
        override val selfUrl: URI,
        override val backendUrl: URI
    ) : ServerConnectionDetails
}
