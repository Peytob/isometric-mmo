package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface UserCrudService {

    fun createUser(externalUserId: String): User

    fun findUserByExternalId(externalUserId: String): User?

    fun isUserExistsByExternalId(externalUserId: String): Boolean

    fun getUsersPage(pageable: Pageable): Page<User>

    fun findUserById(userId: UUID): User?
}
