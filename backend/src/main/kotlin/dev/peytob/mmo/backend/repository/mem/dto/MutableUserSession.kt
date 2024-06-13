package dev.peytob.mmo.backend.repository.mem.dto

import java.time.Instant
import java.util.UUID

data class MutableUserSession(
    val userId: UUID,
    val token: String,
    var expiresAt: Instant,
    val createdAt: Instant
)
