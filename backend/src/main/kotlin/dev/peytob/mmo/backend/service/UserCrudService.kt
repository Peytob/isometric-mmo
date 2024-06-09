package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface UserCrudService {

    fun createUser(username: String, passwordHash: ByteArray): User

    fun findUserById(userId: String): User?

    fun isUserExistsByUsername(userId: String): Boolean

    fun getUsersPage(pageable: Pageable): Page<User>

    fun findUserById(userId: UUID): User?
}
