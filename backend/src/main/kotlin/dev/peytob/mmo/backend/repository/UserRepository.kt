package dev.peytob.mmo.backend.repository

import dev.peytob.mmo.backend.repository.entity.UserEntity

interface UserRepository : BaseRepository<UserEntity> {

    fun existsByExternalId(externalId: String): Boolean

    fun findByExternalId(externalUserId: String): UserEntity
}
