package dev.peytob.mmo.backend.repository.jpa

import dev.peytob.mmo.backend.repository.jpa.entity.UserEntity
import dev.peytob.mmo.backend.service.dto.User

interface UserRepository : BaseRepository<UserEntity> {

    fun existsByUsername(userId: String): Boolean

    fun findByUsername(username: String): User?
}
