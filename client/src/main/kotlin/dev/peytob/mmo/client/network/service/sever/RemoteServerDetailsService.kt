package dev.peytob.mmo.client.network.service.sever

import dev.peytob.mmo.client.network.model.dto.ServerConnectionDetailsResult
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class RemoteServerDetailsService : ServerDetailsService {

    override fun getServerDetails(unauthorizedWebClient: WebClient): Mono<ServerConnectionDetailsResult> {
        TODO("Not yet implemented")
    }
}