package dev.peytob.mmo.backend.configuration.security

import dev.peytob.mmo.backend.service.UserManagementService
import dev.peytob.mmo.backend.service.UserSessionService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component

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
            ?: throw UsernameNotFoundException("User with given username not found")

        val token = userSessionService.startUserSession(user).token

        return PreAuthenticatedAuthenticationToken(token, "", emptyList())
    }

    override fun supports(authentication: Class<*>): Boolean = authentication.isAssignableFrom(UsernamePasswordAuthenticationToken::class.java)
}