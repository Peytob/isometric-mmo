package dev.peytob.mmo.backend.service.dto

import java.time.Instant
import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val registrationTimestamp: Instant,
    val passwordHash: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (registrationTimestamp != other.registrationTimestamp) return false
        return passwordHash.contentEquals(other.passwordHash)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + registrationTimestamp.hashCode()
        result = 31 * result + passwordHash.contentHashCode()
        return result
    }
}
