package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User

interface UserCrudService {

    fun createUser(externalUserId: String): User

    fun findUserByExternalId(externalUserId: String): User?

    fun isUserExistsByExternalId(externalUserId: String): Boolean
}
