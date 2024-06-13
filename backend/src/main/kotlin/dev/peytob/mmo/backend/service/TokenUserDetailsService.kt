package dev.peytob.mmo.backend.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class TokenUserDetailsService(
    private val userSessionService: UserSessionService,
    private val userManagementService: UserManagementService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = loadUserByToken(username)

    private fun loadUserByToken(token: String): UserDetails {
        val activeSession = userSessionService.getTokenActiveSession(token)
            ?: throw UsernameNotFoundException("Session with given token not found")

        val user = userManagementService.findUserByUserId(activeSession.userId)
            ?: throw UsernameNotFoundException("Session user not found")

        return User.builder()
            .username(user.username)
            .password(token)
            .accountLocked(false)
            .accountExpired(false)
            .roles("USER") // TODO Roles...
            .build()
    }
}