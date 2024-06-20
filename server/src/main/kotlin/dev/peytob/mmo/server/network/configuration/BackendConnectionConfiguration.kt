package dev.peytob.mmo.server.network.configuration

import dev.peytob.mmo.server.network.service.ServerDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class BackendConnectionConfiguration {

    @Bean("backendRestClient")
    fun backendRestClient(serverDetailsService: ServerDetailsService): RestClient {
        val serverConnectionDetails = serverDetailsService.getServerConnectionDetails()
        return RestClient.builder()
            .baseUrl(serverConnectionDetails.backendUrl.toString())
            .defaultHeader(serverConnectionDetails.backendToken)
            .build()
    }
}
