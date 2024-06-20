package dev.peytob.mmo.server.network.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ServerUserDetailsService(
    private val userAccessService: UserAccessService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = loadUserByToken(username)

    private fun loadUserByToken(token: String): UserDetails {
        val user = userAccessService.findBackendUserByToken(token)
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