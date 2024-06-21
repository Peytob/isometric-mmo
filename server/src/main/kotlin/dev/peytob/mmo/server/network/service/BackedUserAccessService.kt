package dev.peytob.mmo.server.network.service

import dev.peytob.mmo.core.network.BACKEND_AUTHORIZATION_HEADER
import dev.peytob.mmo.server.network.model.backend.BackendUser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

private val log = LoggerFactory.getLogger(BackedUserAccessService::class.java)

@Service
class BackedUserAccessService(
    @Qualifier("backendRestClient")
    private val backendRestClient: RestClient
) : UserAccessService {

    override fun findBackendUserById(userId: String): BackendUser? {
        log.info("Getting backend user with id {} information", userId)

        return backendRestClient.get()
            .uri("/user/id/{}", userId)
            .retrieve()
            .body(BackendUser::class.java)
    }

    override fun findBackendUserByToken(token: String): BackendUser? {
        log.info("Getting backend user session information")

        return backendRestClient.get()
            .uri("/user/me")
            .header(BACKEND_AUTHORIZATION_HEADER, token)
            .retrieve()
            .body(BackendUser::class.java)
    }
}
