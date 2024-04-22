package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User

interface UserManagementService {

    fun registerUser(externalUserId: String): User
}
