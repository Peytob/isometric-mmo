package dev.peytob.mmo.client.network.service.sever

import dev.peytob.mmo.client.network.model.dto.ServerConnectionDetailsResult
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface ServerDetailsService {

    fun getServerDetails(unauthorizedWebClient: WebClient): Mono<ServerConnectionDetailsResult>
}
