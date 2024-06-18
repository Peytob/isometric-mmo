package dev.peytob.mmo.client.network.service.sever

import dev.peytob.mmo.client.network.model.dto.ServerConnectionDetailsResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private val log = LoggerFactory.getLogger(RemoteServerDetailsService::class.java)

@Service
class RemoteServerDetailsService : ServerDetailsService {

    override fun getServerDetails(unauthorizedWebClient: WebClient): Mono<ServerConnectionDetailsResult> {
        log.info("Getting server details")

        return unauthorizedWebClient.get()
            .uri("/details")
            .retrieve()
            .bodyToMono(ServerConnectionDetailsResult::class.java)
    }
}
