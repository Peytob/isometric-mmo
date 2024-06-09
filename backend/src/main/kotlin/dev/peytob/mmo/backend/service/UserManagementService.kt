package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User

interface UserManagementService {

    fun registerUser(userRegistrationData: UserRegistrationData): User

    fun findUserData(userId: String): User?

    data class UserRegistrationData(
        val username: String,
        val password: String
    )
}
