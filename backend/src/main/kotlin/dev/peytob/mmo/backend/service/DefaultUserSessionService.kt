package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.repository.mem.UserSessionInMemoryRepository
import dev.peytob.mmo.backend.service.dto.User
import dev.peytob.mmo.backend.service.dto.UserSession
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

private val log = LoggerFactory.getLogger(DefaultUserSessionService::class.java)

@Service
class DefaultUserSessionService(
    private val userSessionInMemoryRepository: UserSessionInMemoryRepository
) : UserSessionService {

    private companion object {
        private val DEFAULT_SESSION_DURATION = Duration.ofHours(1)
    }

    override fun getUserActiveSession(user: User): UserSession? = withTokenLazyCheck {
        userSessionInMemoryRepository.getSessionByUserId(user.id)
    }

    override fun getTokenActiveSession(token: String): UserSession? = withTokenLazyCheck {
        userSessionInMemoryRepository.getSessionByToken(token)
    }

    override fun startUserSession(user: User): UserSession {
        log.info("Starting new user {} session", user.id)

        val token = RandomStringUtils.randomAlphabetic(16)

        val createdAt = Instant.now()

        val userSession = MutableUserSession(
            userId = user.id,
            token = token,
            expiresAt = createdAt.plus(DEFAULT_SESSION_DURATION),
            createdAt = createdAt
        )

        return userSessionInMemoryRepository.saveUserSession(userSession)
    }

    override fun endUserActiveSession(user: User) {
        val userSession = userSessionInMemoryRepository.getSessionByUserId(user.id)

        if (userSession != null) {
            log.info("Ending user {} session", user.id)
            userSessionInMemoryRepository.removeUserSession(userSession)
        }
    }

    private fun withTokenLazyCheck(tokenExtractFunction: () -> UserSession?): UserSession? {
        val session = tokenExtractFunction() ?: return null

        if (session.expiresAt.isBefore(Instant.now())) {
            log.info("Getting expired session for user {}. Performing lazy ending session.", session.userId)
            userSessionInMemoryRepository.removeUserSession(session)
            return null
        }

        return session
    }

    private data class MutableUserSession(
        override val userId: UUID,
        override val token: String,
        override val expiresAt: Instant,
        override val createdAt: Instant
    ) : UserSession
}

