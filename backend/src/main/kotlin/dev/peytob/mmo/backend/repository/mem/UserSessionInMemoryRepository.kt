package dev.peytob.mmo.backend.repository.mem

import dev.peytob.mmo.backend.service.dto.UserSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private val log = LoggerFactory.getLogger(UserSessionInMemoryRepository::class.java)

/**
 * The system is not the most reliable, but it is suitable for the first implementation of authorization.
 */
@Component
class UserSessionInMemoryRepository {

    private val sessionsByUser: ConcurrentHashMap<UUID, UserSession> = ConcurrentHashMap(64)
    private val sessionsByToken: ConcurrentHashMap<String, UserSession> = ConcurrentHashMap(64)

    fun removeUserSession(userSession: UserSession): UserSession? {
        log.debug("Removing session for user with id {}", userSession.userId)
        sessionsByUser.remove(userSession.userId)
        sessionsByToken.remove(userSession.token)
        return userSession
    }

    fun getSessionByUserId(userId: UUID): UserSession? {
        return sessionsByUser[userId]
    }

    fun getSessionByToken(token: String): UserSession? {
        return sessionsByToken[token]
    }

    fun saveUserSession(userSession: UserSession): UserSession {
        log.debug("Saving new session for user with id {}", userSession.userId)
        sessionsByUser[userSession.userId] = userSession
        sessionsByToken[userSession.token] = userSession
        return userSession
    }
}
