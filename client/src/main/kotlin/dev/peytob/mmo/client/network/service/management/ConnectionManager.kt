package dev.peytob.mmo.client.network.service.management

import com.fasterxml.jackson.databind.ObjectMapper
import dev.peytob.mmo.client.network.model.ServerConnection
import dev.peytob.mmo.client.network.model.ServerConnectionDetails
import dev.peytob.mmo.client.network.service.backend.BackendAuthorizationService
import dev.peytob.mmo.client.network.service.sever.ServerDetailsService
import dev.peytob.mmo.core.network.BACKEND_AUTHORIZATION_HEADER
import dev.peytob.mmo.core.network.model.DefaultConnectionTypes
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI

private val log = LoggerFactory.getLogger(ConnectionManager::class.java)

@Service
class ConnectionManager(
    private val backendAuthorizationService: BackendAuthorizationService,
    private val serverDetailsService: ServerDetailsService,
    objectMapper: ObjectMapper
) {

    private var serverConnection: ServerConnection? = null

    private var jsonExchangeStrategies = ExchangeStrategies
        .builder()
        .codecs {
            it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, APPLICATION_JSON))
            it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, APPLICATION_JSON))
        }.build()

    fun getServerConnection(): ServerConnection {
        if (!isConnectedToServer()) {
            throw IllegalStateException("No active server connection found")
        }

        return serverConnection!!
    }

    fun isConnectedToServer(): Boolean = serverConnection != null

    fun connectToServer(serverBaseUrl: URI, username: String, password: String): Mono<ServerConnection> {
        log.info("Starting server {} connecting process as {}", serverBaseUrl, username)

        return Mono.just(ServerConnectionBuilder())
            .doOnNext {
                log.debug("Creating server {} unauthorized server web client", serverBaseUrl)

                val serverWebClient = WebClient.builder()
                    .exchangeStrategies(jsonExchangeStrategies)
                    .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .baseUrl(serverBaseUrl.toString())
                    .build()

                it.serverWebClient = serverWebClient
            }.zipWhen {
                log.debug("Getting server {} details", serverBaseUrl)
                val serverDetails = serverDetailsService.getServerDetails(it.serverWebClient!!)
                serverDetails
            }.map {
                val serverConnectionBuilder = it.t1
                val serverDetails = it.t2

                log.debug("Updating server {} connection. Server backend URL: {}, server URL: {}", serverBaseUrl, serverDetails.backendUrl, serverDetails.serverUrl)

                serverConnectionBuilder.backendWebClient = WebClient.builder()
                    .exchangeStrategies(jsonExchangeStrategies)
                    .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .baseUrl(serverDetails.backendUrl.toString())
                    .build()

                serverConnectionBuilder.serverWebClient = serverConnectionBuilder.serverWebClient!!.mutate()
                    .defaultHeader(serverDetails.serverUrl.toString())
                    .build()

                serverConnectionBuilder.serverConnectionDetails = ServerConnectionDetails(
                    serverDetails.serverUrl,
                    serverDetails.backendUrl
                )

                serverConnectionBuilder
            }.zipWhen {
                log.info("Performing backend login as {}", username)
                backendAuthorizationService.login(username, password, it.backendWebClient!!)
            }.map {
                val serverConnectionBuilder = it.t1
                val backendLoginResult = it.t2

                log.debug("Updating all server {} connection data. Welcome to server, sir!", serverConnectionBuilder.serverConnectionDetails?.serverUrl!!)

                serverConnectionBuilder.serverWebClient = serverConnectionBuilder.serverWebClient!!.mutate()
                    .defaultHeader(BACKEND_AUTHORIZATION_HEADER, backendLoginResult.token)
                    .build()

                serverConnectionBuilder.backendWebClient = serverConnectionBuilder.backendWebClient!!.mutate()
                    .defaultHeader(BACKEND_AUTHORIZATION_HEADER, backendLoginResult.token)
                    .build()

                ServerConnection(
                    serverConnectionBuilder.backendWebClient!!,
                    serverConnectionBuilder.serverWebClient!!,
                    DefaultConnectionTypes.WEB_SOCKET,
                    serverConnectionBuilder.serverConnectionDetails!!
                )
            }
    }

    private data class ServerConnectionBuilder(
        var backendWebClient: WebClient? = null,
        var serverWebClient: WebClient? = null,
        var serverConnectionDetails: ServerConnectionDetails? = null
    )
}
