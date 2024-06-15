package dev.peytob.mmo.client.network.service.management

import com.fasterxml.jackson.databind.ObjectMapper
import dev.peytob.mmo.client.network.model.ServerConnection
import dev.peytob.mmo.client.network.service.backend.BackendAuthorizationService
import dev.peytob.mmo.core.network.model.ConnectionType
import dev.peytob.mmo.core.network.model.DefaultConnectionTypes
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI


@Service
class ConnectionManager(
    private val backendAuthorizationService: BackendAuthorizationService,
    objectMapper: ObjectMapper
) {

    private var serverConnection: MutableServerConnection? = null

    private var jsonExchangeStrategies = ExchangeStrategies
        .builder()
        .codecs {
            it.defaultCodecs()
                .jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, APPLICATION_JSON))
            it.defaultCodecs()
                .jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, APPLICATION_JSON))
        }.build()

    fun getServerConnection(): ServerConnection {
        if (!isConnectedToServer()) {
            throw IllegalStateException("No active server connection found")
        }

        return serverConnection!!
    }

    fun isConnectedToServer(): Boolean = serverConnection != null

    fun connectToServer(serverBaseUrl: URI, username: String, password: String) {
        var backendWebClient = WebClient.builder()
            .exchangeStrategies(jsonExchangeStrategies)
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .baseUrl(serverBaseUrl.toString())
            .build()

        val token = backendAuthorizationService.login(username, password, backendWebClient)

//        backendWebClient = backendWebClient.mutate()
//            .defaultHeader("Authorization", token)
//            .build()

        serverConnection = MutableServerConnection(
            connectionType = DefaultConnectionTypes.WEB_SOCKET,
            backendWebClient = backendWebClient
        )
    }

    private data class MutableServerConnection(
        override var backendWebClient: WebClient,
        override var connectionType: ConnectionType
    ) : ServerConnection
}
