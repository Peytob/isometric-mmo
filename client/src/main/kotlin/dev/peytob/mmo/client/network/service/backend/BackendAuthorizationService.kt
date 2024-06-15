package dev.peytob.mmo.client.network.service.backend

import dev.peytob.mmo.client.network.model.dto.BackendLoginResult
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface BackendAuthorizationService {

    fun login(username: String, password: String, unauthorizedWebClient: WebClient): Mono<BackendLoginResult>
}
