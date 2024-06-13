package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User
import java.util.*

interface UserManagementService {

    fun registerUser(userRegistrationData: UserRegistrationData): User

    fun findUserByCredentials(username: String, password: String): User?

    fun findUserByUserId(userId: UUID): User?

    fun findUserByUsername(username: String): User?

    data class UserRegistrationData(
        val username: String,
        val password: String
    )
}
