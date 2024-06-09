package dev.peytob.mmo.backend.repository

import dev.peytob.mmo.backend.repository.entity.UserEntity

interface UserRepository : BaseRepository<UserEntity> {

    fun isUserExistsById(userId: String): Boolean

    fun findUserById(userId: String): UserEntity
}
