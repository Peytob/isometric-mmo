package dev.peytob.mmo.backend.configuration.security

import dev.peytob.mmo.backend.service.UserManagementService
import dev.peytob.mmo.backend.service.UserSessionService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class TokenAuthenticationProvider(
    private val userSessionService: UserSessionService,
    private val userManagementService: UserManagementService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val usernamePasswordAuthentication = authentication as UsernamePasswordAuthenticationToken

        val username = usernamePasswordAuthentication.name.toString()
        val password = usernamePasswordAuthentication.credentials.toString()

        val user = userManagementService.findUserByCredentials(username, password)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User with given credentials not found")

        val token = userSessionService.startUserSession(user).token

        return PreAuthenticatedAuthenticationToken(token, "", emptyList())
    }

    override fun supports(authentication: Class<*>): Boolean = authentication.isAssignableFrom(UsernamePasswordAuthenticationToken::class.java)
}