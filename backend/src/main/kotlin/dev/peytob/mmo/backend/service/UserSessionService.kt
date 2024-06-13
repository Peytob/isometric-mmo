package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User
import dev.peytob.mmo.backend.service.dto.UserSession

interface UserSessionService {

    fun getUserActiveSession(user: User): UserSession?

    fun getTokenActiveSession(token: String): UserSession?

    fun startUserSession(user: User): UserSession

    fun endUserActiveSession(user: User)

    fun endUserActiveSession(userSession: UserSession)
}
