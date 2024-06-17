package dev.peytob.mmo.client.network.service.backend

import dev.peytob.mmo.client.network.model.dto.BackendLoginResult
import dev.peytob.mmo.core.network.BACKEND_AUTHORIZATION_HEADER
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private val log = LoggerFactory.getLogger(RemoteBackendAuthorizationService::class.java)

@Service
class RemoteBackendAuthorizationService : BackendAuthorizationService {

    override fun login(username: String, password: String, unauthorizedWebClient: WebClient): Mono<BackendLoginResult> {
        log.info("Performing backend login request as {}", username)

        return unauthorizedWebClient.post()
            .uri("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(BackendLoginDto(username, password))
            .retrieve()
            .toEntity(Void::class.java)
            .mapNotNull { it.headers[BACKEND_AUTHORIZATION_HEADER]?.firstOrNull() }
            .map { BackendLoginResult(token = it!!) } // Not null due to mapNotNull
    }

    private data class BackendLoginDto(
        val username: String,
        val password: String
    )
}
